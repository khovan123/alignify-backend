package com.api.controller.websocket.admin;

import java.security.Principal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.api.config.EnvConfig;
import com.api.dto.CommonPageRequest;
import com.api.dto.UserDTO;
import com.api.model.User;
import com.api.model.UserBan;
import com.api.repository.UserBanRepository;
import com.api.repository.UserRepository;
import com.api.security.StompPrincipal;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBanRepository userBanRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
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
            userBan.setRoleId(user.getRoleId());
            userBan.setReasonId("SpamId");
            userBan.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            userBanRepository.save(userBan);
            messagingTemplate.convertAndSend("/topic/users/" + userId, userBan);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @MessageMapping("/checkBanned/{userId}")
    public void checkIfBanned(@DestinationVariable("userId") String userId, Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied for: " + userId);
        }

        if (principal instanceof StompPrincipal) {
            Optional<UserBan> userBanOpt = userBanRepository.findById(userId);
            if (userBanOpt.isPresent())
                messagingTemplate.convertAndSend("/topic/users/" + userId,
                        userBanOpt.get());
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @MessageMapping("/users/influencers/normal")
    public void getNormalInfluencers(
            @Payload CommonPageRequest pageRequest,
            Principal principal) {
        if (principal instanceof StompPrincipal) {
            List<UserBan> userBans = userBanRepository.findAllByRoleId(EnvConfig.INFLUENCER_ROLE_ID);
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
            Page<User> users = userRepository
                    .findAllByRoleIdAndNotIn(
                            EnvConfig.INFLUENCER_ROLE_ID,
                            userBans.stream().map(userBan -> userBan.getUserId()).toList(),
                            pageable);
            List<UserDTO> userDTOs = users.getContent().stream().map(user -> {
                return new UserDTO(user.getUserId(), user.getName(), user.getAvatarUrl(), user.getCreatedAt());
            }).toList();
            messagingTemplate.convertAndSend("/topic/users/influencers/normal", userDTOs);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @MessageMapping("/users/influencers/banned")
    public void getBannedInfluencers(
            @Payload CommonPageRequest pageRequest,
            Principal principal) {
        if (principal instanceof StompPrincipal) {
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
            Page<UserBan> userBans = userBanRepository.findAllByRoleId(EnvConfig.INFLUENCER_ROLE_ID, pageable);
            List<User> users = userRepository
                    .findAllByUserIdIn(userBans.getContent().stream().map(userBan -> userBan.getUserId()).toList());
            List<UserDTO> userDTOs = users.stream().map(user -> {
                return new UserDTO(user.getUserId(), user.getName(), user.getAvatarUrl(), user.getCreatedAt());
            }).toList();
            messagingTemplate.convertAndSend("/topic/users/influencers/banned", userDTOs);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @MessageMapping("/users/brands/normal")
    public void getBrands(
            @Payload CommonPageRequest pageRequest,
            Principal principal) {
        if (principal instanceof StompPrincipal) {
            List<UserBan> userBans = userBanRepository.findAllByRoleId(EnvConfig.BRAND_ROLE_ID);
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
            Page<User> users = userRepository
                    .findAllByRoleIdAndNotIn(
                            EnvConfig.BRAND_ROLE_ID,
                            userBans.stream().map(userBan -> userBan.getUserId()).toList(),
                            pageable);
            List<UserDTO> userDTOs = users.getContent().stream().map(user -> {
                return new UserDTO(user.getUserId(), user.getName(), user.getAvatarUrl(), user.getCreatedAt());
            }).toList();
            messagingTemplate.convertAndSend("/topic/users/brands/normal", userDTOs);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @MessageMapping("/users/brands/banned")
    public void getBannedBrands(
            @Payload CommonPageRequest pageRequest,
            Principal principal) {
        if (principal instanceof StompPrincipal) {
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
            Page<UserBan> userBans = userBanRepository.findAllByRoleId(EnvConfig.BRAND_ROLE_ID, pageable);
            List<User> users = userRepository
                    .findAllByUserIdIn(userBans.getContent().stream().map(userBan -> userBan.getUserId()).toList());
            List<UserDTO> userDTOs = users.stream().map(user -> {
                return new UserDTO(user.getUserId(), user.getName(), user.getAvatarUrl(), user.getCreatedAt());
            }).toList();
            messagingTemplate.convertAndSend("/topic/users/brands/banned", userDTOs);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }
}
