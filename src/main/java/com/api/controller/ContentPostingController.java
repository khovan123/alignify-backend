package com.api.controller;

import com.api.model.ContentPosting;
import com.api.service.ContentPostingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/contentPosting")
public class ContentPostingController {

    @Autowired
    ContentPostingService contentPostingSer;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody ContentPosting contentPosting, HttpServletRequest request) {
        return contentPostingSer.createContentPosting(contentPosting, request);
    }

    @GetMapping("/all")
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

    @PutMapping("/{userId}/{contentId}")
    public ResponseEntity<?> updatePost(@PathVariable("contentId") String contentId,
            @PathVariable("userId") String userId,
            @RequestBody ContentPosting contentPosting,
            HttpServletRequest request) {
        return contentPostingSer.updateContentPosting(contentId,userId, contentPosting, request);
    }

    @DeleteMapping("/{userId}/{contentId}")
    public ResponseEntity<?> deletePost(
            @PathVariable("contentId") String contentId,
            @PathVariable("userId") String userId,
            HttpServletRequest request) {

        return contentPostingSer.deleteContentPosting(contentId,userId, request);
    }
    
    @PutMapping("/{contentId}")
    public ResponseEntity<?> toggleLike(@PathVariable("contentId") String contentId,
            HttpServletRequest request) {
        return contentPostingSer.toggleLike(contentId,request);
    }

}
