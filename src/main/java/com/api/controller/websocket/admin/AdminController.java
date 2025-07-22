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
import com.api.dto.response.AdminStatisticsResponse;
import com.api.dto.response.CampaignResponse;
import com.api.dto.response.ContentPostingResponse;
import com.api.model.Campaign;
import com.api.model.ContentPosting;
import com.api.model.Permission;
import com.api.model.Reason;
import com.api.model.User;
import com.api.model.UserBan;
import com.api.repository.CampaignRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.ContentPostingRepository;
import com.api.repository.PermissionRepository;
import com.api.repository.UserBanRepository;
import com.api.repository.UserRepository;
import com.api.security.StompPrincipal;
import com.api.service.ContentPostingService;

@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBanRepository userBanRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ContentPostingRepository contentPostingRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ContentPostingService contentPostingService;

    // @PreAuthorize("hasRole('ROLE_ADMIN')")
    @MessageMapping("/ban/{userId}")
    public void banUser(
            @DestinationVariable("userId") String userId,
            @Payload Reason reason,
            Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied for: " + userId);
        }

        if (principal instanceof StompPrincipal) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                throw new IllegalArgumentException("User not found: " + userId);
            }

            User user = optionalUser.get();
            user.setActive(false);
            userRepository.save(user);
            UserBan userBan = new UserBan();
            userBan.setUserId(userId);
            userBan.setRoleId(user.getRoleId());
            userBan.setReasonId(reason.getReasonId());
            userBan.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            userBanRepository.save(userBan);
            messagingTemplate.convertAndSend("/topic/users/" + userId, userBan);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    @MessageMapping("/unban/{userId}")
    public void unbanUser(@DestinationVariable("userId") String userId, Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new SecurityException("Access is denied for: " + userId);
        }

        if (principal instanceof StompPrincipal) {
            Optional<User> optionalUser = userRepository.findById(userId);
            if (!optionalUser.isPresent()) {
                throw new IllegalArgumentException("User not found: " + userId);
            }

            User user = optionalUser.get();
            user.setActive(true);
            userRepository.save(user);
            userBanRepository.deleteById(userId);
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
                List<Permission> permissions = permissionRepository.findByPermissionIdIn(user.getPermissionIds());
                return new UserDTO(user.getUserId(), user.getName(), user.getAvatarUrl(), permissions,
                        user.getCreatedAt());
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
                List<Permission> permissions = permissionRepository.findByPermissionIdIn(user.getPermissionIds());
                return new UserDTO(user.getUserId(), user.getName(), user.getAvatarUrl(), permissions,
                        user.getCreatedAt());
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

    @MessageMapping("/campaigns")
    public void getAllCampaigns(
            @Payload CommonPageRequest pageRequest,
            Principal principal) {
        if (principal instanceof StompPrincipal) {
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
            Page<Campaign> campaignPage = campaignRepository.findAll(pageable);
            List<CampaignResponse> campaignResponses = campaignPage.getContent().stream().map(campaign -> {
                User user = userRepository.findByUserId(campaign.getBrandId()).get();
                return new CampaignResponse(user, campaign, categoryRepository);
            }).toList();
            messagingTemplate.convertAndSend("/topic/campaigns", campaignResponses);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    @MessageMapping("/contents")
    public void getAllContents(
            @Payload CommonPageRequest pageRequest,
            Principal principal) {
        if (principal instanceof StompPrincipal) {
            Pageable pageable = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize());
            Page<ContentPosting> contentsPage = contentPostingRepository.findAll(pageable);
            List<ContentPostingResponse> contentResponse = contentsPage.getContent().stream()
                    .map(content -> contentPostingService.mapToDTO(
                            content))
                    .toList();
            messagingTemplate.convertAndSend("/topic/contents", contentResponse);
        } else {
            throw new SecurityException("Invalid principal type");
        }
    }

    @MessageMapping("/admin/statistics/overview")
    public void getAdminStatisticsOverview(Principal principal) {
        if (principal instanceof StompPrincipal) {
            long totalUsers = userRepository.count();
            long totalCampaigns = campaignRepository.countByStatusNot("DRAFT");
            long totalContentPostings = contentPostingRepository.count();

            AdminStatisticsResponse response = new AdminStatisticsResponse(
                totalUsers, totalCampaigns, totalContentPostings
            );
            messagingTemplate.convertAndSend("/topic/admin/statistics/overview", response);
        } else {
            System.out.println("[AdminStatistics] principal=" + (principal == null ? "null" : principal.getClass().getName()));
            throw new SecurityException("Invalid principal type");
        }
    }
}
