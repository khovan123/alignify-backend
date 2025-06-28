package com.api.controller.websocket.comment;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

import com.api.dto.CommonPageRequest;
import com.api.dto.response.CommentResponse;
import com.api.model.Comment;
import com.api.model.ContentPosting;
import com.api.model.User;
import com.api.repository.CommentRepository;
import com.api.repository.ContentPostingRepository;
import com.api.repository.UserRepository;
import com.api.security.StompPrincipal;

@Controller
public class CommentController {
  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private SimpMessagingTemplate messagingTemplate;
  @Autowired
  private ContentPostingRepository contentPostingRepository;
  @Autowired
  private UserRepository userRepository;

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

  @MessageMapping("/comment/select/{contentId}")
  public void getComments(
      @DestinationVariable("contentId") String contentId,
      @Payload CommonPageRequest pageRequest,
      Principal principal) {
    if (principal == null || principal.getName() == null) {
      throw new SecurityException("Access is denied at: " + contentId);
    }
    if (principal instanceof StompPrincipal stompPrincipal) {
      Optional<ContentPosting> contentOpt = contentPostingRepository.findById(contentId);
      if (!contentOpt.isPresent()) {
        throw new SecurityException("Access is denied at: " + contentId);
      }
      int pageNumber = pageRequest.getPageNumber();
      int pageSize = pageRequest.getPageSize();
      PageRequest pagerequest = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
      Page<Comment> commentPage = commentRepository.findAllByContentId(contentId, pagerequest);
      List<CommentResponse> commentResponses = commentPage.getContent().stream()
          .map(comment -> {
            Optional<User> userOpt = userRepository.findByUserId(comment.getUserId());
            if (userOpt.isPresent()) {
              User user = userOpt.get();
              return new CommentResponse(user.getName(), user.getAvatarUrl(), comment);
            }
            return null;
          })
          .filter(
              Objects::nonNull)
          .toList();

      messagingTemplate.convertAndSend("/topic/comments/select/" + contentId, commentResponses);
      return;
    }
    throw new SecurityException("Invalid principal type");
  }
}
