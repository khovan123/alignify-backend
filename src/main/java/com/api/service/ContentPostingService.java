package com.api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.dto.ApiResponse;
import com.api.dto.response.ContentPostingResponse;
import com.api.model.Category;
import com.api.model.Comment;
import com.api.model.ContentPosting;
import com.api.model.User;
import com.api.repository.CategoryRepository;
import com.api.repository.CommentRepository;
import com.api.repository.ContentPostingRepository;
import com.api.repository.LikesRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;

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
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public ResponseEntity<?> createContentPosting(
            ContentPosting contentPosting,
            MultipartFile file,
            CustomUserDetails userDetails,
            HttpServletRequest request) {
        String imageUrl;
        try {
            imageUrl = fileStorageService.storeFile(file);
        } catch (Exception e) {
            return ApiResponse.sendError(500, e.getMessage(), request.getRequestURI());
        }
        contentPosting.setUserId(userDetails.getUserId());
        contentPosting.setImageUrl(imageUrl);
        contentPosting = contentPostingRepo.save(contentPosting);
        messagingTemplate.convertAndSend("/topic/contents/post", mapToDTO(contentPosting));
        return ApiResponse.sendSuccess(201, "Content posting created successfully", mapToDTO(contentPosting),
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
        User user = userRepository.findByUserId(post.getUserId()).orElse(null);
        ContentPostingResponse dto = new ContentPostingResponse();
        long likeCount = likesRepo.countByContentId(post.getContentId());
        long commentCount = commentRepository.countByContentId(post.getContentId());
        if (commentCount != post.getCommentCount()) {
            post.setCommentCount((int) commentCount);
        }
        if (likeCount != post.getLikeCount()) {
            post.setLikeCount((int) likeCount);
        }
        dto.setContentId(post.getContentId());
        dto.setContentName(post.getContentName());
        if (user != null) {
            dto.setUserAvatar(user.getAvatarUrl());
            dto.setUserName(user.getName());
        }
        dto.setUserId(post.getUserId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCategories(categoryInfo);
        dto.setCreatedDate(post.getCreatedDate());
        dto.setPublic(post.isIsPublic());
        dto.setCommentCount(comments.size());
        dto.setLikeCount((long) post.getLikeCount());
        contentPostingRepo.save(post);
        return dto;
    }

    public ResponseEntity<?> getAllContentPostings(HttpServletRequest request, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ContentPosting> posts = contentPostingRepo.findByIsPublicTrue(pageable);

        List<ContentPostingResponse> dtoList = posts.getContent().stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> getContentPostingById(String userId, HttpServletRequest request,
                                                   int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ContentPosting> posts = contentPostingRepo.findByUserIdAndIsPublicTrue(userId, pageable);

        List<ContentPostingResponse> dtoList = posts.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> getContentPostingByPostId(
            String postId,
            HttpServletRequest request) {
        ContentPosting post = contentPostingRepo.findById(postId).get();
        return ApiResponse.sendSuccess(200, "Success", mapToDTO(post), request.getRequestURI());
    }

    public ResponseEntity<?> getMe(CustomUserDetails userDetails, HttpServletRequest request,
                                   int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<ContentPosting> posts = contentPostingRepo.findByUserId(userDetails.getUserId(), pageable);

        List<ContentPostingResponse> dtoList = posts.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> deleteContentPosting(String contentId, CustomUserDetails userDetails,
                                                  HttpServletRequest request) {
        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();
            contentPostingRepo.deleteById(contentId);
            likesRepo.deleteAllByContentId(contentId);
            commentRepository.deleteAllByContentId(contentId);
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

    public ResponseEntity<?> getPostByCategoryIds(
            int pageNumber,
            int pageSize,
            String categoryIds,
            HttpServletRequest request) {

        if (categoryIds == null || categoryIds.isEmpty()) {
            return ApiResponse.sendError(400, "Category list must not be empty", request.getRequestURI());
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<ContentPosting> postPage = contentPostingRepo.findByCategoryIdsInOrderByCreatedDateDesc(categoryIds,
                pageable);

        List<ContentPostingResponse> dtoList = postPage.getContent().stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> updateContentPosting(
            String contentId,
            ContentPosting updatedContentPosting,
            MultipartFile file,
            CustomUserDetails userDetails,
            HttpServletRequest request) {

        Optional<ContentPosting> contentPostingOpt = contentPostingRepo.findById(contentId);
        if (contentPostingOpt.isPresent()) {
            ContentPosting contentPosting = contentPostingOpt.get();
            if (updatedContentPosting.getContentName() != null) {
                contentPosting.setContentName(updatedContentPosting.getContentName());
            }
            if (updatedContentPosting.getContent() != null) {
                contentPosting.setContent(updatedContentPosting.getContent());
            }
            String imageUrl = null;
            if (file != null) {
                try {
                    imageUrl = fileStorageService.storeFile(file);
                } catch (Exception e) {
                    return ApiResponse.sendError(500, e.getMessage(), request.getRequestURI());
                }
            }
            if (imageUrl != null) {
                contentPosting.setImageUrl(imageUrl);
            }
            if (!updatedContentPosting.getCategoryIds().isEmpty() && updatedContentPosting.getCategoryIds() != null) {
                contentPosting.setCategoryIds(updatedContentPosting.getCategoryIds());
            } else {
                contentPosting.setCategoryIds(new ArrayList<>());
            }
            contentPosting.setIsPublic(updatedContentPosting.isIsPublic());

            contentPostingRepo.save(contentPosting);

            return ApiResponse.sendSuccess(200, "Content posting updated successfully", contentPosting,
                    request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "Content posting not found", request.getRequestURI());
        }
    }

    // public ResponseEntity<?> toggleLike(String contentId, HttpServletRequest
    // request) {
    // Optional<ContentPosting> contentPostingOpt =
    // contentPostingRepo.findById(contentId);
    // if (contentPostingOpt.isEmpty()) {
    // return ApiResponse.sendError(404, "Content posting not found",
    // request.getRequestURI());
    // }
    // ContentPosting content = contentPostingOpt.get();
    // String userId = JwtUtil.decodeToken(request).getSubject();
    // boolean isOwner = content.getUserId().equals(userId);
    // boolean isPublic = content.isIsPublic();
    // if (!isPublic && !isOwner) {
    // return ResponseEntity.status(403).body(
    // Map.of("error", "You do not have permission to like this private content."));
    // }
    // Optional<Likes> existingLike = likesRepo.findByUserIdAndContentId(userId,
    // contentId);
    // if (existingLike.isPresent()) {
    // likesRepo.deleteByUserIdAndContentId(userId, contentId);
    // } else {
    // Likes newLike = new Likes(userId, contentId);
    // likesRepo.save(newLike);
    // }
    // long likeCount = likesRepo.countByContentId(contentId);
    // content.setLikeCount((int) likeCount);
    // contentPostingRepo.save(content);
    // String message = existingLike.isPresent() ? "Like removed" : "Like added";
    // return ApiResponse.sendSuccess(
    // 200,
    // message,
    // Map.of("likeCount", likeCount),
    // request.getRequestURI());
    // }
    public ResponseEntity<?> findContentByTerm(String term, int pageNumber, int pageSize, HttpServletRequest request) {
        if (term == null || term.trim().isEmpty()) {
            return ApiResponse.sendSuccess(200, "No content found", Collections.emptyList(), request.getRequestURI());
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<String> userIds = userRepository.findByNameContainingIgnoreCase(term)
                .stream()
                .map(user -> user.getUserId())
                .toList();

        Page<ContentPosting> contentPage;
        if (!userIds.isEmpty()) {
            contentPage = contentPostingRepo.findByUserIdInAndIsPublicTrue(userIds, pageable);
        } else {
            contentPage = contentPostingRepo.findByContentNameContainingIgnoreCaseAndIsPublicTrue(term, pageable);
        }

        List<ContentPostingResponse> dtoList = contentPage.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        return ApiResponse.sendSuccess(200, "Search content success", dtoList, request.getRequestURI());
    }

    public ContentPosting convertToContentPosting(String obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(obj, ContentPosting.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid campaign JSON: " + e.getMessage(), e);
        }
    }

}
