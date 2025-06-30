package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.model.ContentPosting;
import com.api.security.CustomUserDetails;
import com.api.service.ContentPostingService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/contentPosting")
public class ContentPostingController {

    @Autowired
    ContentPostingService contentPostingSer;

    @PreAuthorize("hasRole('ROLE_INFLUENCER')")
    @PostMapping("")
    public ResponseEntity<?> createPost(
            @RequestPart("contentPosting") String obj,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return contentPostingSer.createContentPosting(contentPostingSer.convertToContentPosting(obj), image,
                userDetails,
                request);
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

    @GetMapping("/filterByCategory/{categoryId}")
    public ResponseEntity<?> getPostByCategoryId(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request){
        return contentPostingSer.getPostByCategoryIds(pageNumber, pageSize, categoryId, request);
    }
    
    
    @GetMapping("/me")
    public ResponseEntity<?> getMe(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
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
    
    // @PutMapping("/{contentId}/like")
    // public ResponseEntity<?> toggleLike(@PathVariable("contentId") String
    // contentId,
    // HttpServletRequest request) {
    // return contentPostingSer.toggleLike(contentId, request);
    // }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/search")
    public ResponseEntity<?> searchContent(
            @RequestParam("term") String term,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            HttpServletRequest request) {
        return contentPostingSer.findContentByTerm(term, pageNumber, pageSize, request);
    }

}
