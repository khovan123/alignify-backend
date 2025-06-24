package com.api.controller.websocket.notification;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;

import com.api.model.Notification;
import com.api.repository.NotificationRepository;

public class NotificationController {

  @Autowired
  private NotificationRepository notificationRepository;

  @MessageMapping("/notify/{userId}")
  @SendTo("/topic/notifications/{userId}")
  public Notification sendNotification(
      @Payload Notification notification,
      @DestinationVariable("userId") String userId,
      Principal principal) {
    if (principal == null || principal.getName() == null) {
      throw new SecurityException("Access is denied for: " + userId);
    }
    return notification;
  }

}
