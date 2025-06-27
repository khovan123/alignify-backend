package com.api.controller.websocket.likes;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.api.model.ContentPosting;
import com.api.model.Likes;
import com.api.repository.ContentPostingRepository;
import com.api.repository.LikesRepository;
import com.api.security.StompPrincipal;

@Controller
public class LikesController {
  @Autowired
  private LikesRepository likesRepository;
  @Autowired
  private SimpMessagingTemplate messagingTemplate;
  @Autowired
  ContentPostingRepository contentPostingRepository;

  @MessageMapping("/like/{contentId}")
  public void toggleLike(
      @Payload Likes likes,
      @DestinationVariable("contentId") String contentId,
      Principal principal) {
    if (principal == null || principal.getName() == null) {
      throw new SecurityException("Access is denied for: " + contentId);
    }
    Optional<ContentPosting> contentPostingOpt = contentPostingRepository.findById(contentId);
    if (!contentPostingOpt.isPresent()) {
      throw new SecurityException("Not found for: " + contentId);
    }
    ContentPosting contentPosting = contentPostingOpt.get();
    if (principal instanceof StompPrincipal stompPrincipal) {
      if (likesRepository.existsByContentIdAndUserId(contentId, stompPrincipal.getUserId())) {
        likesRepository.deleteByUserIdAndContentId(stompPrincipal.getUserId(), contentId);
        contentPosting.setLikeCount(contentPosting.getLikeCount() - 1);
      } else {
        likes.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        likesRepository.save(likes);
        contentPosting.setLikeCount(contentPosting.getLikeCount() + 1);
      }
      contentPostingRepository.save(contentPosting);
      messagingTemplate.convertAndSend("/topic/contents/" + contentId, Map.of("likes", contentPosting.getLikeCount()));
    } else {
      throw new SecurityException("Invalid principal type");
    }
  }

}
