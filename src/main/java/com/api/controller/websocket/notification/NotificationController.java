package com.api.controller.websocket.notification;

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

import com.api.model.Notification;
import com.api.model.User;
import com.api.repository.NotificationRepository;
import com.api.repository.UserRepository;
import com.api.security.StompPrincipal;

@Controller
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/notify/{userId}")
    public void sendNotification(
            @Payload Notification notification,
            @DestinationVariable("userId") String userId,
            Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied for: " + userId);
        }
        if (principal instanceof StompPrincipal stompPrincipal) {

            if (userId.equals(((StompPrincipal) principal).getUserId())) {
                notification.setUserId(userId);
                if (notification.getAvatarUrl().isEmpty())
                    notification.setAvatarUrl(stompPrincipal.getAvatarUrl());
                if (notification.getName().isEmpty())
                    notification.setName(stompPrincipal.getName());
            } else {
                Optional<User> userOpt = userRepository.findById(userId);
                if (!userOpt.isPresent()) {
                    throw new SecurityException("Access is denied for: " + userId);
                }
                User user = userOpt.get();
                notification.setUserId(user.getUserId());
                if (notification.getAvatarUrl().isEmpty())
                    notification.setAvatarUrl(user.getAvatarUrl());
                if (notification.getName().isEmpty())
                    notification.setName(user.getName());
            }
            notification.setRead(false);
            notification.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

}
