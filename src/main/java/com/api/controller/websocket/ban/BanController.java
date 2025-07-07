package com.api.controller.websocket.ban;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.api.model.User;
import com.api.repository.UserRepository;
import com.api.security.StompPrincipal;

@Controller
public class BanController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ban/{userId}")
    public void toggleBanUser(@DestinationVariable("userId") String userId, Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied for: " + userId);
        }

        if (principal instanceof StompPrincipal stompPrincipal) {
            // Optional: Check if the requester is actually an admin (by roleId)
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) {
                throw new IllegalArgumentException("User not found: " + userId);
            }

            User user = optionalUser.get();
            user.setActive(!user.isActive()); // Toggle isActive
            userRepository.save(user);

            // Notify clients
            messagingTemplate.convertAndSend("/topic/users/" + userId,
                    Map.of("isActive", user.isActive()));
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    @MessageMapping("/isBanned/{userId}")
    public void checkIfBanned(@DestinationVariable("userId") String userId, Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied for: " + userId);
        }

        if (principal instanceof StompPrincipal) {
            Optional<User> optionalUser = userRepository.findById(userId);
            boolean isBanned = optionalUser.map(user -> !user.isActive()).orElse(false);
            messagingTemplate.convertAndSend("/topic/users/isBanned/" + userId,
                    Map.of("isBanned", isBanned));
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }
}
