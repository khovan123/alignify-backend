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
    public ResponseEntity<?> getAllPosts(HttpServletRequest request) {
        return contentPostingSer.getAllContentPostings(request);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPostsByUserId(@PathVariable String userId, HttpServletRequest request) {
        return contentPostingSer.getContentPostingById(userId, request);
    }

    @PutMapping("/{contentId}/{userId}")
    public ResponseEntity<?> updatePost(@PathVariable String contentId,
            @PathVariable String userId,
            @RequestBody ContentPosting contentPosting,
            HttpServletRequest request) {
        return contentPostingSer.updateContentPosting(contentId, userId, contentPosting, request);
    }

    @DeleteMapping("/{contentId}/{userId}")
    public ResponseEntity<?> deletePost(
            @PathVariable String contentId,
            @PathVariable String userId,
            HttpServletRequest request) {

        return contentPostingSer.deleteContentPosting(contentId, userId, request);
    }

}
