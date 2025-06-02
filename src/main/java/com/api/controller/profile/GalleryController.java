package com.api.controller.profile;

import com.api.service.GalleryService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/profile/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getGallery(@PathVariable("id") String id, HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "30") int pageSize) {
        DecodedJWT decodeJWT = JwtUtil.decodeToken(request);
        String userId = decodeJWT.getSubject();
        User user;
        try {
            user = userRepository.findById(id).get();
        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Profile not found."));
        }
        if (!user.getRoleId().equalsIgnoreCase(EnvConfig.INFLUENCER_ROLE_ID)) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Gallery not found."));
        }
        if (!influencerProfileRepository.isPublic(userId) && !Helper.isOwner(id, request)) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", "Profile is private."));
        }
        try {
            Optional<Gallery> galleryOpt = galleryRepository.findById(id);
            if (galleryOpt.isPresent() && !galleryOpt.get().getImages().isEmpty()
                    && galleryOpt.get().getImages() != null) {
                List<String> imageIds = galleryOpt.get().getImages();
                List<Image> images = imageRepository
                        .findTop9ByIdInOrderByUploadedAtDesc(imageIds, PageRequest.of(pageNumber, pageSize)).stream()
                        .sorted(Comparator.comparing(Image::getCreateAt).reversed())
                        .limit(pageSize)
                        .collect(Collectors.toList());
                if (!images.isEmpty()) {
                    return ResponseEntity.status(200).body(Map.of(
                            "data", Map.of(
                                    "gallery", images,
                                    "pageNumber", pageNumber,
                                    "pageSize", pageSize)));
                }
            }

        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of(
                    "error", e.getMessage()));
        }
        return ResponseEntity.status(404).body(Map.of(
                "error", "Profile is private."));
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> postImage(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        return galleryService.saveImageUrlIntoGalleryById(id, file, request);
    }

    @DeleteMapping("/{id}/image/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") String id, @PathVariable("imageId") String imageId) {
        System.out.println("hello");
        return ResponseEntity.status(200).body(Map.of(
                "id", id,
                "imageId", imageId));
    }
}
