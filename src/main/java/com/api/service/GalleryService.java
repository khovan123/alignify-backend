package com.api.service;

import com.api.dto.*;
import com.api.model.*;
import com.api.repository.*;
import com.api.security.CustomUserDetails;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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

    public ResponseEntity<?> getGallery(int pageNumber,
            int pageSize, CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getUserId();
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

    public ResponseEntity<?> saveGalleryImageIntoGallery(MultipartFile file, CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getUserId();
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

    public ResponseEntity<?> deleteGalleryImageByImageId(String galleryImageId, CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getUserId();
        Optional<GalleryImage> imageOpt = imageRepository.findById(galleryImageId);
        if (!imageOpt.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }
        imageRepository.delete(imageOpt.get());
        return ApiResponse.sendSuccess(204, null, null, request.getRequestURI());
    }

    public ResponseEntity<?> getGalleryImageByImageId(String galleryImageId, CustomUserDetails userDetails, HttpServletRequest request) {
        String id = userDetails.getUserId();
        Optional<Influencer> influencer = influencerProfileRepository.findById(id);
        if (!influencer.isPresent()) {
            return ApiResponse.sendError(404, id + " does not exist", request.getRequestURI());
        }

        Optional<GalleryImage> imageOpt = imageRepository.findById(galleryImageId);
        if (imageOpt.isPresent()) {
            return ApiResponse.sendSuccess(200, "", Map.of(
                    "galleryId", id,
                    "image", imageOpt.get()
            ), request.getRequestURI()
            );
        } else {
            return ApiResponse.sendError(404, galleryImageId + " does not exist", request.getRequestURI());
        }
    }
}
