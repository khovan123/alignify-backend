package com.api.controller.websocket.likes;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.api.model.Likes;
import com.api.repository.LikesRepository;
import com.api.security.StompPrincipal;

public class LikesController {
  @Autowired
  private LikesRepository likesRepository;
  @Autowired
  private SimpMessagingTemplate messagingTemplate;

  @MessageMapping("/like/{contentId}")
  public void toggleLike(
      @Payload Likes likes,
      @DestinationVariable("contentId") String contentId,
      Principal principal) {
    if (principal == null || principal.getName() == null) {
      throw new SecurityException("Access is denied for: " + contentId);
    }
    if (principal instanceof StompPrincipal stompPrincipal) {
      if (likesRepository.existsByContentIdAndUserId(contentId, stompPrincipal.getUserId())) {
        likesRepository.deleteByUserIdAndContentId(stompPrincipal.getUserId(), contentId);
      } else {
        likes.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        likesRepository.save(likes);
      }
      messagingTemplate.convertAndSend("/topic/contents/" + contentId, likes);
    } else {
      throw new SecurityException("Invalid principal type");
    }
  }

}
