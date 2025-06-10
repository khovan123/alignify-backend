
package com.api.controller;

import com.api.model.Comment;
import com.api.service.CommentService;
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
@RequestMapping("/api/v1/comment")
public class CommentController {
    @Autowired
    private CommentService commentservice;
    @PostMapping("")
    public ResponseEntity<?> createComment(
            @RequestBody Comment comment,
            HttpServletRequest request
    ){
        return commentservice.createComment(comment, request);
    }
    @GetMapping("contentId/{contentId}")
    public ResponseEntity<?> getCommentByContentId(
            @PathVariable("contentId") String contentId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return commentservice.getCommentByContentId(contentId,request, pageNumber, pageSize);
    }
    @GetMapping("userId/{userId}")
    public ResponseEntity<?> getCommentByUserId(
            @PathVariable("userId") String userId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize){
        return commentservice.getCommentByUserId(userId, request, pageNumber, pageSize);
    }
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable("commentId") String commentId,
            @RequestBody Comment comment,
            HttpServletRequest request){
        return commentservice.updateComment(commentId, comment, request);
    }
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable("commentId") String commentId,
            HttpServletRequest request){
        return commentservice.deleteComment(commentId, request);
    }
}
