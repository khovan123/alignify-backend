package com.api.service;

import com.api.config.EnvConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.dto.ApiResponse;
import com.api.dto.response.ContentPostingResponse;
import com.api.model.Category;
import com.api.model.Comment;
import com.api.model.ContentPosting;
import com.api.model.Likes;
import com.api.repository.CategoryRepository;
import com.api.repository.CommentRepository;
import com.api.repository.LikesRepository;
import com.api.security.CustomUserDetails;
import com.api.util.Helper;
import com.api.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import com.api.repository.ContentPostingRepository;

@Service
public class ContentPostingService {

    @Autowired
    private ContentPostingRepository contentPostingRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private LikesRepository likesRepo;
    @Autowired
    private CommentRepository commentRepository;

    public ResponseEntity<?> createContentPosting(ContentPosting contentPosting, CustomUserDetails userDetails, HttpServletRequest request) {
        if (userDetails.getRoleId().equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            contentPosting = contentPostingRepo.save(contentPosting);
            return ApiResponse.sendSuccess(201, "Content posting created successfully", contentPosting,
                    request.getRequestURI());
        }
        return ApiResponse.sendError(403, "Content Posting only create by Influencer", request.getRequestURI());
    }

    public ContentPostingResponse mapToDTO(ContentPosting post) {
        List<Category> categories = categoryRepo.findAllByCategoryIdIn(post.getCategoryIds());

        List<Map<String, String>> categoryInfo = categories.stream()
                .map(cat -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("categoryId", cat.getCategoryId());
                    map.put("categoryName", cat.getCategoryName());
                    return map;
                })
                .toList();

        List<Comment> comments = commentRepository.findAllByContentId(post.getContentId());

        ContentPostingResponse dto = new ContentPostingResponse();
        dto.setContentId(post.getContentId());
        dto.setUserId(post.getUserId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCategories(categoryInfo);
        dto.setCreatedDate(post.getCreatedDate());
        dto.setPublic(post.isIsPublic());
        dto.setCommentCount(comments.size());
        dto.setLikeCount(post.getLikeCount());
        return dto;
    }

    public ResponseEntity<?> getAllContentPostings(HttpServletRequest request, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ContentPosting> posts = contentPostingRepo.findByIsPublicTrue(pageable);

        List<ContentPostingResponse> dtoList = posts.getContent().stream()
                .map(this::mapToDTO)
                .toList();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", posts.getNumber());
        responseData.put("totalPages", posts.getTotalPages());
        responseData.put("totalItems", posts.getTotalElements());
        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> getContentPostingById(String userId, HttpServletRequest request,
            int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ContentPosting> posts = contentPostingRepo.findByUserIdAndIsPublicTrue(userId, pageable);

        List<ContentPostingResponse> dtoList = posts.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", posts.getNumber());
        responseData.put("totalPages", posts.getTotalPages());
        responseData.put("totalItems", posts.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> getMe(CustomUserDetails userDetails, HttpServletRequest request,
            int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ContentPosting> posts = contentPostingRepo.findByUserId(userDetails.getUserId(), pageable);

        List<ContentPostingResponse> dtoList = posts.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", posts.getNumber());
        responseData.put("totalPages", posts.getTotalPages());
        responseData.put("totalItems", posts.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> deleteContentPosting(String contentId, CustomUserDetails userDetails,
            HttpServletRequest request) {
        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();

            if (!contentPosting.getUserId().equals(userDetails)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied. You are not the owner of this content."));
            }

            contentPostingRepo.deleteById(contentId);
            return ApiResponse.sendSuccess(
                    204,
                    "Content posting deleted successfully",
                    null,
                    request.getRequestURI());
        } else {
            return ApiResponse.sendError(
                    404,
                    "Content posting not found",
                    request.getRequestURI());
        }
    }

    public ResponseEntity<?> updateContentPosting(String contentId, CustomUserDetails userDetails,
            ContentPosting updatedContentPosting,
            HttpServletRequest request) {

        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();

            if (!Helper.isOwner(userDetails.getUserId(), request)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied. You are not the owner of this content."));
            }

            List<String> updatedCategoryIds = updatedContentPosting.getCategoryIds();
            List<Category> validCategories = categoryRepo.findAllByCategoryIdIn(updatedCategoryIds);

            if (validCategories == null || validCategories.isEmpty()) {
                return ApiResponse.sendError(400, "No valid category IDs provided", request.getRequestURI());
            }

            List<String> validCategoryIds = validCategories.stream()
                    .map(Category::getCategoryId)
                    .toList();

            contentPosting.setContent(updatedContentPosting.getContent());
            contentPosting.setImageUrl(updatedContentPosting.getImageUrl());
            contentPosting.setCategoryIds(validCategoryIds);
            contentPosting.setIsPublic(updatedContentPosting.isIsPublic());
            contentPosting.setCommentCount(updatedContentPosting.getCommentCount());
            contentPosting.setLikeCount(updatedContentPosting.getLikeCount());

            contentPostingRepo.save(contentPosting);

            return ApiResponse.sendSuccess(200, "Content posting updated successfully", contentPosting,
                    request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "Content posting not found", request.getRequestURI());
        }
    }

    public ResponseEntity<?> toggleLike(String contentId, HttpServletRequest request) {
        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isEmpty()) {
            return ApiResponse.sendError(404, "Content posting not found", request.getRequestURI());
        }

        ContentPosting content = contentPostingOpt.get();
        String userId = JwtUtil.decodeToken(request).getSubject();

        boolean isOwner = content.getUserId().equals(userId);
        boolean isPublic = content.isIsPublic();

        if (!isPublic && !isOwner) {
            return ResponseEntity.status(403).body(
                    Map.of("error", "You do not have permission to like this private content."));
        }

        Optional<Likes> existingLike = likesRepo.findByUserIdAndContentId(userId, contentId);

        if (existingLike.isPresent()) {
            likesRepo.deleteByUserIdAndContentId(userId, contentId);
        } else {
            Likes newLike = new Likes(userId, contentId);
            likesRepo.save(newLike);
        }

        long likeCount = likesRepo.countByContentId(contentId);
        content.setLikeCount((int) likeCount);
        contentPostingRepo.save(content);

        String message = existingLike.isPresent() ? "Like removed" : "Like added";

        return ApiResponse.sendSuccess(
                200,
                message,
                Map.of("likeCount", likeCount),
                request.getRequestURI());
    }

}
