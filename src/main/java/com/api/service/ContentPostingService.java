package com.api.service;

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
import com.api.repository.IContentPostingRepository;
import com.api.repository.LikesRepository;
import com.api.security.CustomUserDetails;
import com.api.util.Helper;
import com.api.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ContentPostingService {

    @Autowired
    private IContentPostingRepository contentPostingRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private LikesRepository likesRepo;
    @Autowired
    private CommentRepository commentRepository;

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

        List<Comment> comments = commentRepository.findAllByContentId(post.getContentId());

        ContentPostingResponse dto = new ContentPostingResponse();
        dto.setContentId(post.getContentId());
        dto.setUserId(post.getUserId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCategories(categoryInfo);
        dto.setTimestamp(post.getTimestamp());
        dto.setIsPublic(post.isIsPublic());
        dto.setCommentCount(comments.size());
        dto.setLike(post.getLike());

        return dto;
    }

    public ResponseEntity<?> getAllContentPostings(HttpServletRequest request, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ContentPosting> contentPostings = contentPostingRepo.findByIsPublicTrue(pageable);

        List<ContentPostingResponse> dtoList = contentPostings.stream()
                .map(this::mapToDTO)
                .toList();

        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> getContentPostingById(CustomUserDetails userDetails, HttpServletRequest request,
            int pageNumber, int pageSize) {
        String userId = userDetails.getId();
        List<ContentPosting> posts = contentPostingRepo.findByUserId(userId);

        if (posts.isEmpty()) {
            return ApiResponse.sendError(404, "No content postings found for userId: " + userId,
                    request.getRequestURI());
        }

        boolean isOwner = Helper.isOwner(userId, request);

        List<ContentPosting> visiblePosts = isOwner
                ? posts
                : posts.stream().filter(ContentPosting::isIsPublic).toList();

        int start = pageNumber * pageSize;
        int end = Math.min(start + pageSize, visiblePosts.size());

        if (start >= visiblePosts.size()) {
            return ApiResponse.sendSuccess(200, "No more content", List.of(), request.getRequestURI());
        }

        List<ContentPostingResponse> dtoList = visiblePosts.subList(start, end).stream()
                .map(this::mapToDTO)
                .toList();

        return ApiResponse.sendSuccess(200, "Content postings fetched successfully", dtoList, request.getRequestURI());
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

            if (!Helper.isOwner(userDetails.getId(), request)) {
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
        content.setLike((int) likeCount);
        contentPostingRepo.save(content);

        String message = existingLike.isPresent() ? "Like removed" : "Like added";

        return ApiResponse.sendSuccess(
                200,
                message,
                Map.of("likeCount", likeCount),
                request.getRequestURI());
    }

}
