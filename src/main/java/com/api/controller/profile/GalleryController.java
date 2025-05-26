package com.api.controller.profile;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.api.config.*;
import com.api.model.*;
import com.api.repository.*;
import com.api.util.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/profile/gallery")
public class GalleryController {

    @Autowired
    private InfluencerRepository influencerProfileRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private GalleryRepository galleryRepository;
    @Autowired
    private ImageRepository imageRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> getGallery(@PathVariable("id") String id, HttpServletRequest request, @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "30") int pageSize) {
        DecodedJWT decodeJWT = JwtUtil.decodeToken(request);
        String userId = decodeJWT.getSubject();
        User user;
        try {
            user = userRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Profile not found."
            ));
        }
        if (!user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Gallery not found."
            ));
        }
        if (!influencerProfileRepository.isPublic(userId) && !Helper.isOwner(id, request)) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Profile is private."
            ));
        }
        try {
            Optional<Gallery> galleryOpt = galleryRepository.findById(id);
            if (galleryOpt.isPresent() && !galleryOpt.get().getImages().isEmpty() && galleryOpt.get().getImages() != null) {
                List<String> imageIds = galleryOpt.get().getImages();
                List<Image> images = imageRepository.findTop9ByIdInOrderByUploadedAtDesc(imageIds, PageRequest.of(pageNumber, pageSize)).stream()
                        .sorted(Comparator.comparing(Image::getCreateAt).reversed())
                        .limit(pageSize)
                        .collect(Collectors.toList());
                if (!images.isEmpty()) {
                    return ResponseEntity.status(200).body(Map.of(
                            "data", Map.of(
                                    "gallery", images,
                                    "pageNumber", pageNumber,
                                    "pageSize", pageSize)
                    ));
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()
            ));
        }
        return ResponseEntity.status(404).body(Map.of(
                "error", "Profile is private."
        ));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> postImageIntoGallery(@PathVariable("id") String id, @RequestBody Image image, HttpServletRequest request) {
        if (!Helper.isOwner(id, request)) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Access is denied."
            ));
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

        Image savedImage = imageRepository.save(image);

        gallery.getImages().add(savedImage.getImageId());
        galleryRepository.save(gallery);
        return ResponseEntity.status(200).body(Map.of(
                "message", "Gallery updated."
        ));
    }

    @DeleteMapping("/{id}/image/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") String id, @PathVariable("imageId") String imageId) {
        System.out.println("hello");
        return ResponseEntity.status(200).body(Map.of(
                "id", id,
                "imageId", imageId
        ));
    }
}
