package com.api.controller.websocket.ban;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.api.model.User;
import com.api.model.UserBan;
import com.api.repository.UserBanRepository;
import com.api.repository.UserRepository;
import com.api.security.StompPrincipal;

@Controller
public class BanController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBanRepository userBanRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/ban/{userId}")
    public void banUser(@DestinationVariable("userId") String userId, Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied for: " + userId);
        }

        if (principal instanceof StompPrincipal) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                throw new IllegalArgumentException("User not found: " + userId);
            }

            User user = optionalUser.get();
            user.setActive(!user.isActive());
            userRepository.save(user);
            UserBan userBan = new UserBan();
            userBan.setUserId(userId);
            userBan.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            userBanRepository.save(userBan);
            messagingTemplate.convertAndSend("/topic/users/" + userId, userBan);
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
