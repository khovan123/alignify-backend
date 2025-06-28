package com.api.controller.websocket.comment;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.api.dto.response.CommentResponse;
import com.api.model.Comment;
import com.api.model.ContentPosting;
import com.api.repository.CommentRepository;
import com.api.repository.ContentPostingRepository;
import com.api.security.StompPrincipal;

@Controller
public class CommentController {
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private SimpMessagingTemplate messagingTemplate;
  @Autowired
  private ContentPostingRepository contentPostingRepository;

  @MessageMapping("/comment/{contentId}")
  public void sendComment(
      @Payload Comment comment,
      @DestinationVariable("contentId") String contentId,
      Principal principal) {
    if (principal == null || principal.getName() == null) {
      throw new SecurityException("Access is denied at: " + contentId);
    }
    if (principal instanceof StompPrincipal stompPrincipal) {
      Optional<ContentPosting> contentOpt = contentPostingRepository.findById(contentId);
      if (!contentOpt.isPresent()) {
        throw new SecurityException("Access is denied at: " + contentId);
      }
      ContentPosting content = contentOpt.get();
      if (comment.getCreatedDate() == null) {
        comment.setCreatedDate(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
      }
      content.setCommentCount(content.getCommentCount() + 1);
      contentPostingRepository.save(content);
      commentRepository.save(comment);
      messagingTemplate.convertAndSend("/topic/comments/" + contentId,
          new CommentResponse(stompPrincipal.getName(), stompPrincipal.getAvatarUrl(), comment));
    }
    throw new SecurityException("Invalid principal type");
  }
}