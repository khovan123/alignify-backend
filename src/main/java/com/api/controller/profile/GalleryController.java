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
        return galleryService.getGalleryById(id, request, pageNumber, pageSize);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> postImage(@PathVariable("id") String id, @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        return galleryService.saveImageUrlIntoGalleryById(id, file, request);
    }

    @DeleteMapping("/{id}/image/{imageId}")
    public ResponseEntity<?> deleteImage(@PathVariable("id") String id, @PathVariable("imageId") String imageId,
            HttpServletRequest request) {
        return galleryService.deleteImageByImageId(id, imageId, request);
    }

    @GetMapping("/{id}/image/{imageId}")
    public ResponseEntity<?> getImage(@PathVariable("id") String id, @PathVariable("imageId") String imageId,
            HttpServletRequest request) {
        return galleryService.getImageByImageId(id, imageId, request);
    }
}
