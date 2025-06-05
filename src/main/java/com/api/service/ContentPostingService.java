package com.api.service;

import com.api.dto.ApiResponse;
import com.api.dto.ContentPostingResponse;
import com.api.model.Category;
import com.api.model.ContentPosting;
import com.api.model.Likes;
import com.api.repository.CategoryRepository;
import com.api.repository.IContentPostingRepository;
import com.api.repository.LikesRepository;
import com.api.util.Helper;
import com.api.util.JwtUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private LikesRepository likesRepo;

    public ResponseEntity<?> createContentPosting(ContentPosting contentPosting, HttpServletRequest request) {
        contentPosting = contentPostingRepo.save(contentPosting);
        return ApiResponse.sendSuccess(201, "Content posting created successfully", contentPosting,
                request.getRequestURI());
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

    ContentPostingResponse dto = new ContentPostingResponse();
    dto.setContentId(post.getContentId());
    dto.setUserId(post.getUserId());
    dto.setContent(post.getContent());
    dto.setImageUrl(post.getImageUrl());
    dto.setCategories(categoryInfo); 
    dto.setTimestamp(post.getTimestamp());
    dto.setIsPublic(post.isIsPublic());
    dto.setCommentIds(post.getCommentIds());
    dto.setLike(post.getLike());

    return dto;
}


    public ResponseEntity<?> getAllContentPostings(HttpServletRequest request) {
        List<ContentPosting> contentPostings = contentPostingRepo.findByIsPublicTrue();
        List<ContentPostingResponse> dtoList = contentPostings.stream()
                .map(this::mapToDTO)
                .toList();

        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> getContentPostingById(String userId, HttpServletRequest request) {
        List<ContentPosting> posts = contentPostingRepo.findByUserId(userId);

        if (posts.isEmpty()) {
            return ApiResponse.sendError(404, "No content postings found for userId: " + userId, request.getRequestURI());
        }

        boolean isOwner = Helper.isOwner(userId, request);

        List<ContentPosting> visiblePosts = isOwner ? posts
                : posts.stream().filter(ContentPosting::isIsPublic).toList();

        List<ContentPostingResponse> dtoList = visiblePosts.stream()
                .map(this::mapToDTO)
                .toList();

        return ApiResponse.sendSuccess(200, "Content postings fetched successfully", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> deleteContentPosting(String contentId, String userId, HttpServletRequest request) {
        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();

            if (!contentPosting.getUserId().equals(userId)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied. You are not the owner of this content."));
            }

            contentPostingRepo.deleteById(contentId);
            return ApiResponse.sendSuccess(
                    204,
                    "Content posting deleted successfully",
                    null,
                    request.getRequestURI()
            );
        } else {
            return ApiResponse.sendError(
                    404,
                    "Content posting not found",
                    request.getRequestURI()
            );
        }
    }

    public ResponseEntity<?> updateContentPosting(String contentId, String user,
            ContentPosting updatedContentPosting,
            HttpServletRequest request) {

        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();

            if (!Helper.isOwner(contentPosting.getUserId(), request)) {
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
            contentPosting.setCommentIds(updatedContentPosting.getCommentIds());
            contentPosting.setLike(updatedContentPosting.getLike());

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

    String userId = JwtUtil.decodeToken(request).getSubject();
    Optional<Likes> existingLike = likesRepo.findByUserIdAndContentId(userId, contentId);

    if (existingLike.isPresent()) {
        likesRepo.deleteByUserIdAndContentId(userId, contentId);
    } else {
        Likes newLike = new Likes(userId, contentId);
        likesRepo.save(newLike);
    }

    long likeCount = likesRepo.countByContentId(contentId);

    ContentPosting content = contentPostingOpt.get();
    content.setLike((int) likeCount);
    contentPostingRepo.save(content); 

    String message = existingLike.isPresent() ? "Like removed" : "Like added";

    return ApiResponse.sendSuccess(
        200, message,
        Map.of("likeCount", likeCount),
        request.getRequestURI()
    );
}


}


