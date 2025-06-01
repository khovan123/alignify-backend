package com.api.service;

import com.api.dto.ApiResponse;
import com.api.model.*;
import com.api.repository.*;
import com.api.util.Helper;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class GalleryService {

    @Autowired
    private InfluencerRepository influencerProfileRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private GalleryImageRepository imageRepository;
    @Autowired
    private Cloudinary cloudinary;
    @Value("${cloudinary.upload-preset}")
    private String uploadPreset;

    public ResponseEntity<?> getGalleryById(String id, HttpServletRequest request, @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "30") int pageSize) {
        Optional<Influencer> influencerOpt = influencerProfileRepository.findById(id);
        if (!influencerOpt.get().isPublic() && !Helper.isOwner(id, request)) {
            return ApiResponse.sendError(403, "Access denied: Insufficient permissions", request.getRequestURI());
        }
        Optional<Gallery> galleryOpt = galleryRepository.findById(id);
        if (!galleryOpt.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }
        try {
            List<String> imageIds = galleryOpt.get().getImages();
            List<GalleryImage> images = imageRepository.findTop9ByIdInOrderByUploadedAtDesc(imageIds, PageRequest.of(pageNumber, pageSize)).stream()
                    .sorted(Comparator.comparing(GalleryImage::getCreatedAt).reversed())
                    .limit(pageSize)
                    .collect(Collectors.toList());
            return ApiResponse.sendSuccess(200, "", Map.of(
                    "galleryId", id,
                    "images", images
            ), request.getRequestURI()
            );
        } catch (Exception e) {
            return ApiResponse.sendError(500, "Internal server error", request.getRequestURI());
        }
    }

    public ResponseEntity<?> saveImageUrlIntoGalleryById(String id, MultipartFile file, HttpServletRequest request) {
        if (!Helper.isOwner(id, request)) {
            return ApiResponse.sendError(403, "Access denied: Insufficient permissions", request.getRequestURI());
        }
        Optional<Gallery> galleryOtp = galleryRepository.findById(id);
        Gallery gallery;
        if (!galleryOtp.isPresent()) {
            gallery = new Gallery();
            gallery.setGalleryId(id);
            gallery.setImages(new ArrayList<>());
        } else {
            gallery = galleryOtp.get();
            if (gallery.getImages() == null) {
                gallery.setImages(new ArrayList<>());
            }
        }

        String imageUrl = null;
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("upload_preset", uploadPreset));
            imageUrl = (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            return ApiResponse.sendError(500, "Internal server error", request.getRequestURI());
        }
        if (imageUrl == null) {
            return ApiResponse.sendError(500, "Internal server error", request.getRequestURI());
        }
        GalleryImage image = new GalleryImage(imageUrl);
        image = imageRepository.save(image);

        gallery.getImages().add(image.getImageId());
        galleryRepository.save(gallery);

        return ApiResponse.sendSuccess(200, "", Map.of(
                "galleryId", id,
                "image", image
        ), request.getRequestURI()
        );
    }

    public ResponseEntity<?> deleteImageByImageId(String id, String imageId, HttpServletRequest request) {
        if (!Helper.isOwner(id, request)) {
            return ApiResponse.sendError(403, "Access denied: Insufficient permissions", request.getRequestURI());
        }
        Optional<GalleryImage> imageOpt = imageRepository.findById(imageId);
        if (!imageOpt.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }
        imageRepository.delete(imageOpt.get());
        return ApiResponse.sendSuccess(204, null, null, request.getRequestURI());
    }

    public ResponseEntity<?> getImageByImageId(String id, String imageId, HttpServletRequest request) {
        Optional<Influencer> influencer = influencerProfileRepository.findById(id);
        if (!influencer.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }

        if (!influencer.get().isPublic()) {
            return ApiResponse.sendError(403, "Access denied: Insufficient permissions", request.getRequestURI());
        }

        Optional<GalleryImage> imageOpt = imageRepository.findById(imageId);
        if (imageOpt.isPresent()) {
            return ApiResponse.sendSuccess(200, "", Map.of(
                    "galleryId", id,
                    "image", imageOpt.get()
            ), request.getRequestURI()
            );
        } else {
            return ApiResponse.sendError(404, imageId + " does not exist", request.getRequestURI());
        }
    }
}
