package com.api.controller.websocket.notification;

import java.security.Principal;
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
    // @SendTo("/topic/notifications/{userId}")
    public void sendNotification(
            @Payload Notification notification,
            @DestinationVariable("userId") String userId,
            Principal principal) {
        System.out.println("Received notification: " + notification);
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied for: " + userId);
        }
        if (principal instanceof StompPrincipal stompPrincipal) {

            if (userId.equals(((StompPrincipal) principal).getUserId())) {
                notification.setUserId(userId);
                notification.setAvatarUrl(stompPrincipal.getAvatarUrl());
                notification.setName(stompPrincipal.getName());
                notification.setRead(false);
            } else {
                Optional<User> userOpt = userRepository.findById(userId);
                if (!userOpt.isPresent()) {
                    throw new SecurityException("Access is denied for: " + userId);
                }
                User user = userOpt.get();
                notification.setUserId(user.getUserId());
                notification.setAvatarUrl(user.getAvatarUrl());
                notification.setName(user.getName());
                notification.setRead(false);
            }
            notificationRepository.save(notification);
            messagingTemplate.convertAndSend("/topic/notifications/" + userId, notification);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

}
