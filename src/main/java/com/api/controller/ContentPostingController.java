package com.api.controller;

import com.api.model.ContentPosting;
import com.api.security.CustomUserDetails;
import com.api.service.ContentPostingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contentPosting")
public class ContentPostingController {

    @Autowired
    ContentPostingService contentPostingSer;

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody ContentPosting contentPosting, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        return contentPostingSer.createContentPosting(contentPosting, userDetails, request);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllPosts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return contentPostingSer.getAllContentPostings(request, pageNumber, pageSize);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPostsByUserId(
            @PathVariable("userId") String userId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return contentPostingSer.getContentPostingById(userId, request, pageNumber, pageSize);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ){
        return contentPostingSer.getMe(userDetails, request, pageNumber, pageSize);
    }

    @PutMapping("/{contentId}")

    public ResponseEntity<?> updatePost(@PathVariable("contentId") String contentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ContentPosting contentPosting,
            HttpServletRequest request) {
        return contentPostingSer.updateContentPosting(contentId, userDetails, contentPosting, request);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<?> deletePost(
            @PathVariable("contentId") String contentId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {

        return contentPostingSer.deleteContentPosting(contentId, userDetails, request);
    }

    @PutMapping("/{contentId}/like")
    public ResponseEntity<?> toggleLike(@PathVariable("contentId") String contentId,
            HttpServletRequest request) {
        return contentPostingSer.toggleLike(contentId, request);
    }

}
