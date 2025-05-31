package com.api.service;

import com.api.dto.ApiResponse;
import com.api.model.ContentPosting;
import com.api.repository.IContentPostingRepository;
import com.api.util.Helper;
import com.api.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class ContentPostingService {

    @Autowired
    private IContentPostingRepository contentPostingRepo;

    public ResponseEntity<?> createContentPosting(ContentPosting contentPosting, HttpServletRequest request) {
        contentPosting = contentPostingRepo.save(contentPosting);
        return ApiResponse.sendSuccess(201, "Content posting created successfully", contentPosting,
                request.getRequestURI());
    }

    public ResponseEntity<?> getAllContentPostings(HttpServletRequest request) {
        List<ContentPosting> contentPostings = contentPostingRepo.findAll();
        return ApiResponse.sendSuccess(200, "Success", contentPostings, request.getRequestURI());
    }

    public ResponseEntity<?> getContentPostingById(String userId, HttpServletRequest request) {
        List<ContentPosting> posts = contentPostingRepo.findByInfluencerID(userId);
        if (posts.isEmpty()) {
            return ApiResponse.sendError(404, "No content postings found for userId: " + userId,
                    request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Content postings fetched successfully", posts, request.getRequestURI());
    }

    public ResponseEntity<?> deleteContentPosting(String contentId, HttpServletRequest request) {
        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();

            DecodedJWT decodedJWT = JwtUtil.decodeToken(request);
            String currentUserId = decodedJWT.getSubject();

            if (!Helper.isOwner(contentPosting.getInfluencerID(), request)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied. You are not the owner of this content."));
            }

            contentPostingRepo.deleteById(contentId);
            return ApiResponse.sendSuccess(200, "Content posting deleted successfully", null, request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "Content posting not found", request.getRequestURI());
        }
    }

    public ResponseEntity<?> updateContentPosting(String contentId, ContentPosting updatedContentPosting,
            HttpServletRequest request) {
        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();

            if (!Helper.isOwner(contentPosting.getInfluencerID(), request)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied. You are not the owner of this content."));
            }

            contentPosting.setContent(updatedContentPosting.getContent());
            contentPosting.setImageUrl(updatedContentPosting.getImageUrl());
            contentPosting.setCategoryId(updatedContentPosting.getCategoryId());
            contentPosting.setIsPublic(updatedContentPosting.isIsPublic());
            contentPosting.setCommentId(updatedContentPosting.getCommentId());
            contentPosting.setLike(updatedContentPosting.getLike());

            contentPostingRepo.save(contentPosting);
            return ApiResponse.sendSuccess(200, "Content posting updated successfully", contentPosting,
                    request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "Content posting not found", request.getRequestURI());
        }
    }

    public ResponseEntity<?> incrementLike(String contentId, HttpServletRequest request) {
        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();
            contentPosting.setLike(contentPosting.getLike() + 1);
            contentPostingRepo.save(contentPosting);
            return ApiResponse.sendSuccess(200, "Like incremented", Map.of("like", contentPosting.getLike()),
                    request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "Content posting not found", request.getRequestURI());
        }
    }

}
