package com.api.controller.profile;

import com.api.security.CustomUserDetails;
import com.api.service.GalleryService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/profiles/galleries")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    @GetMapping("")
    public ResponseEntity<?> getGallery(@RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "30") int pageSize, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return galleryService.getGallery(pageNumber, pageSize, userDetails, request);
    }

    @PostMapping("")
    public ResponseEntity<?> postImage(@RequestPart("images") List<MultipartFile> images, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return galleryService.saveGalleryImageIntoGallery(images, userDetails, request);
    }

    @DeleteMapping("/galleries/galleryImages/{galleryImageId}")
    public ResponseEntity<?> deleteImage(@PathVariable("imageId") String galleryImageId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return galleryService.deleteGalleryImageByImageId(galleryImageId, userDetails, request);
    }

    @GetMapping("/galleries/galleryImages/{galleryImageId}")
    public ResponseEntity<?> getImage(@PathVariable("imageId") String galleryImageId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return galleryService.getGalleryImageByImageId(galleryImageId, userDetails, request);
    }
}
