package com.api.service;

import com.api.dto.ApiResponse;
import com.api.model.Comment;
import com.api.model.ContentPosting;
import com.api.repository.CommentRepository;
import com.api.util.Helper;
import jakarta.servlet.http.HttpServletRequest;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.api.repository.ContentPostingRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ContentPostingRepository contentPostingRepo;

    public ResponseEntity<?> createComment(Comment comment, HttpServletRequest request) {
        comment.setCreatedDate(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        comment = commentRepository.save(comment);

        Optional<ContentPosting> contentOpt = contentPostingRepo.findById(comment.getContentId());
        if (contentOpt.isPresent()) {
            ContentPosting content = contentOpt.get();
            if (content.isIsPublic()) {
                int count = content.getCommentCount();
                content.setCommentCount(count + 1);
                contentPostingRepo.save(content);
                return ApiResponse.sendSuccess(201, "Comment created successfully", comment,
                        request.getRequestURI());
            }
        }
        return ApiResponse.sendError(403, "error", request.getRequestURI());
    }

    public ResponseEntity<?> getCommentByUserId(String userId, HttpServletRequest request, int pageNumber,
            int pageSize) {
        if (!Helper.isOwner(userId, request)) {
            return ResponseEntity.status(403).body(
                    Map.of("error", "Access denied. You are not allowed to view these comments."));
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<Comment> comments = commentRepository.findByUserId(userId, pageable);

        if (comments.isEmpty()) {
            return ApiResponse.sendError(404, "No comments found for userId: " + userId, request.getRequestURI());
        }

        return ApiResponse.sendSuccess(200, "Comments retrieved successfully", comments, request.getRequestURI());
    }

    public ResponseEntity<?> getCommentByContentId(String contentId, HttpServletRequest request, int pageNumber,
            int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));

        List<Comment> comments = commentRepository.findByContentId(contentId, pageable);

        if (comments.isEmpty()) {
            return ApiResponse.sendError(404, "No comments found for contentId: " + contentId, request.getRequestURI());
        }

        return ApiResponse.sendSuccess(200, "Comments retrieved successfully", comments, request.getRequestURI());
    }

    public ResponseEntity<?> deleteComment(String commentId, HttpServletRequest request) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            return ApiResponse.sendError(404, "Comment not found", request.getRequestURI());
        }

        Comment comment = commentOpt.get();

        boolean isCommentOwner = Helper.isOwner(comment.getUserId(), request);

        Optional<ContentPosting> contentOpt = contentPostingRepo.findById(comment.getContentId());
        boolean isContentOwner = contentOpt.isPresent() && Helper.isOwner(contentOpt.get().getUserId(), request);

        if (!isCommentOwner && !isContentOwner) {
            return ResponseEntity.status(403).body(
                    Map.of("error",
                            "Access denied. You can only delete your own comment or comments on your content."));
        }

        commentRepository.deleteById(commentId);

        return ApiResponse.sendSuccess(
                204,
                "Comment deleted successfully",
                null,
                request.getRequestURI());
    }

    public ResponseEntity<?> updateComment(String commentId, Comment updatedComment, HttpServletRequest request) {
        Optional<Comment> commentOpt = commentRepository.findById(commentId);

        if (commentOpt.isEmpty()) {
            return ApiResponse.sendError(404, "Comment not found", request.getRequestURI());
        }

        Comment existingComment = commentOpt.get();

        if (!Helper.isOwner(existingComment.getUserId(), request)) {
            return ResponseEntity.status(403).body(
                    Map.of("error", "Access denied. You can only update your own comment."));
        }

        existingComment.setContent(updatedComment.getContent());
        existingComment.setCreatedDate(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

        commentRepository.save(existingComment);

        return ApiResponse.sendSuccess(200, "Comment updated successfully", existingComment, request.getRequestURI());
    }

}
