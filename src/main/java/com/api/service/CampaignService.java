package com.api.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
<<<<<<< Updated upstream

import com.api.config.EnvConfig;
=======
import org.springframework.web.multipart.MultipartFile;
>>>>>>> Stashed changes
import com.api.dto.ApiResponse;
import com.api.dto.response.CampaignResponse;
import com.api.model.Campaign;
import com.api.model.Category;
<<<<<<< Updated upstream
import com.api.model.User;
=======
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.model.User;
import com.api.repository.ApplicationRepository;
>>>>>>> Stashed changes
import com.api.repository.CampaignRepository;
import com.api.repository.CategoryRepository;
<<<<<<< Updated upstream
=======
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
>>>>>>> Stashed changes
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import com.api.util.Helper;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
<<<<<<< Updated upstream
    private UserRepository userRepository;
=======
    private CampaignTrackingRepository campaignTrackingRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    UserRepository userRepository;
>>>>>>> Stashed changes

    public ResponseEntity<?> createCampaign(Campaign campaign, CustomUserDetails userDetails,
            HttpServletRequest request) {
<<<<<<< Updated upstream
        if (userDetails.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            campaign.setUserId(userDetails.getUserId());
            campaign = campaignRepo.save(campaign);
            return ApiResponse.sendSuccess(201, "Campaign posting created successfully", this.mapToDTO(campaign),
                    request.getRequestURI());
        }
        return ApiResponse.sendError(403, "Campaign posting only create by Brand", request.getRequestURI());
=======
        String brandId = userDetails.getUserId();
        if (!(campaign.getStatus().equals("DRAFT") || campaign.getStatus().equals("RECRUITING"))) {
            ApiResponse.sendError(400, "Illegal status", request.getRequestURI());
        }
        String imageUrl;
        try {
            imageUrl = Helper.saveImage(file);
        } catch (Exception e) {
            return ApiResponse.sendError(500, e.getMessage(), request.getRequestURI());
        }
        campaign.setBrandId(brandId);
        campaign.setImageUrl(imageUrl);
        campaign = campaignRepo.save(campaign);
        chatRoomRepository.save(new ChatRoom(campaign.getCampaignId(), brandId, campaign.getCampaignName(), campaign.getImageUrl()));
        User user = userRepository.findById(brandId).get();
        List<String> readBy = new ArrayList<>();
        readBy.add(brandId);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage("Wellcome " + user.getName() + " !");
        chatMessage.setChatRoomId(campaign.getCampaignId());
        chatMessage.setName(user.getName());
        chatMessage.setReadBy(readBy);
        chatMessage.setUserId(brandId);
        chatMessage.setSendAt(LocalDateTime.MIN);
        chatMessageRepository.save(new ChatMessage());
        return ApiResponse.sendSuccess(201, "Campaign posting created successfully",
                new CampaignResponse(campaign, categoryRepo),
                request.getRequestURI());
>>>>>>> Stashed changes
    }

    public CampaignResponse mapToDTO(Campaign post) {
        List<Category> categories = categoryRepo.findAllByCategoryIdIn(post.getCategoryIds());

        List<Map<String, String>> categoryInfo = categories.stream()
                .map(cat -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("categoryId", cat.getCategoryId());
                    map.put("categoryName", cat.getCategoryName());
                    return map;
                })
                .toList();

        CampaignResponse dto = new CampaignResponse();

        dto.setCampaignId(post.getCampaignId());
        dto.setUserId(post.getUserId());
        dto.setContent(post.getContent());
        dto.setImageUrl(post.getImageUrl());
        dto.setCategories(categoryInfo);
        dto.setCreatedDate(post.getCreatedDate());
        dto.setStatus(post.getStatus());
        dto.setBudget(post.getBudget());
        dto.setCampaignRequirements(post.getCampaignRequirements());
        dto.setInfluencerRequirement(post.getInfluencerRequirement());
        dto.setInfluencerCount(post.getInfluencerCount());
        return dto;
    }

    public ResponseEntity<?> getAllCampaign(int pageNumber, int pageSize, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Campaign> campaignPage = campaignRepo.findAll(pageable);

        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", campaignPage.getNumber());
        responseData.put("totalPages", campaignPage.getTotalPages());
        responseData.put("totalItems", campaignPage.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> getMe(CustomUserDetails userDetails, int pageNumber, int pageSize,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Campaign> campaignPage = campaignRepo.findByUserId(userDetails.getUserId(), pageable);
        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", campaignPage.getNumber());
        responseData.put("totalPages", campaignPage.getTotalPages());
        responseData.put("totalItems", campaignPage.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> getCampaignsByUserId(String userId, int pageNumber, int pageSize,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Campaign> campaignPage = campaignRepo.findByUserId(userId, pageable);

        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(this::mapToDTO)
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", campaignPage.getNumber());
        responseData.put("totalPages", campaignPage.getTotalPages());
        responseData.put("totalItems", campaignPage.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> deleteCampaign(String campaignId, CustomUserDetails userDetails,
            HttpServletRequest request) {
        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();
            if (!campaign.getUserId().equals(userDetails.getUserId())) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied. You are not the owner of this content."));
            }
            campaignRepo.deleteById(campaignId);
            return ApiResponse.sendSuccess(
                    204,
                    "campaign posting deleted successfully",
                    null,
                    request.getRequestURI());
        } else {
            return ApiResponse.sendError(
                    404,
                    "Campaign posting not found",
                    request.getRequestURI());
        }
<<<<<<< Updated upstream
=======
        List<CampaignTracking> relatedTrackings = campaignTrackingRepository.findAllByCampaignId(campaignId);
        campaignTrackingRepository.deleteAll(relatedTrackings);
        campaignRepo.deleteById(campaignId);
        chatRoomRepository.deleteById(campaignId);
        return ApiResponse.sendSuccess(
                204,
                "campaign posting and related trackings deleted successfully",
                null,
                request.getRequestURI());
>>>>>>> Stashed changes
    }

    public ResponseEntity<?> updateCampaign(String campaignId, CustomUserDetails userDetails,
            Campaign updatedCampaign,
            HttpServletRequest request) {

        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        User user = userRepository.findById(userDetails.getUserId()).get();
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();
<<<<<<< Updated upstream

            if (!Helper.isOwner(campaign.getUserId(), request)) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied. You are not the owner of this content."));
            }

            List<String> updatedCategoryIds = updatedCampaign.getCategoryIds();
            List<Category> validCategories = categoryRepo.findAllByCategoryIdIn(updatedCategoryIds);

            if (validCategories == null || validCategories.isEmpty()) {
                return ApiResponse.sendError(400, "No valid category IDs provided", request.getRequestURI());
            }

            String newStatus = updatedCampaign.getStatus();
            List<String> validStatuses = List.of("DRAFT", "PENDING", "COMPLETED");
            if (newStatus == null || !validStatuses.contains(newStatus)) {
                return ApiResponse.sendError(400, "Invalid status. Allowed values: DRAFT, PENDING, COMPLETED",
=======
            ChatRoom chatRoom = chatRoomRepository.findById(campaignId).get();
            if ("COMPLETED".equals(campaign.getStatus())) {
                return ApiResponse.sendError(400, "Cannot update a campaign that is COMPLETED",
>>>>>>> Stashed changes
                        request.getRequestURI());
            }
            long newBudget = updatedCampaign.getBudget();
            if (newBudget < 0) {
                return ApiResponse.sendError(400, "Budget must be a non-negative number", request.getRequestURI());
            }
<<<<<<< Updated upstream
            List<String> validCategoryIds = validCategories.stream()
                    .map(Category::getCategoryId)
                    .toList();

            campaign.setContent(updatedCampaign.getContent());
            campaign.setImageUrl(updatedCampaign.getImageUrl());
            campaign.setCategoryIds(validCategoryIds);
            campaign.setStatus(newStatus);
            campaign.setBudget(newBudget);
            campaign.setCampaignRequirements(updatedCampaign.getCampaignRequirements());
            campaign.setInfluencerRequirement(updatedCampaign.getInfluencerRequirement());

=======
            if (updatedCampaign.getCampaignName() != null) {
                campaign.setCampaignName(updatedCampaign.getCampaignName());
                chatRoom.setRoomName(updatedCampaign.getCampaignName());
            }

            if (updatedCampaign.getContent() != null) {
                campaign.setContent(updatedCampaign.getContent());
            }
            if (updatedCampaign.getImageUrl() != null) {
                campaign.setImageUrl(updatedCampaign.getImageUrl());
                chatRoom.setRoomAvatarUrl(updatedCampaign.getImageUrl());
            }
            if (updatedCampaign.getCategoryIds() != null && !updatedCampaign.getCategoryIds().isEmpty()) {
                List<Category> validCategories = categoryRepo.findAllByCategoryIdIn(updatedCampaign.getCategoryIds());
                if (validCategories.isEmpty()) {
                    return ApiResponse.sendError(400, "No valid category IDs provided", request.getRequestURI());
                }
                List<String> validCategoryIds = validCategories.stream()
                        .map(Category::getCategoryId)
                        .toList();
                campaign.setCategoryIds(validCategoryIds);
            }
            if (updatedCampaign.getStatus() != null) {
                campaign.setStatus(updatedCampaign.getStatus());
            }
            if (updatedCampaign.getBudget() > 0) {
                campaign.setBudget(newBudget);
            }
            if (updatedCampaign.getCampaignRequirements() != null
                    && !updatedCampaign.getCampaignRequirements().isEmpty()) {
                campaign.setCampaignRequirements(updatedCampaign.getCampaignRequirements());
            }
            if (updatedCampaign.getInfluencerRequirement() != null
                    && !updatedCampaign.getInfluencerRequirement().isEmpty()) {
                campaign.setInfluencerRequirement(updatedCampaign.getInfluencerRequirement());
            }
            if (updatedCampaign.getInfluencerCountExpected() > 0) {
                campaign.setInfluencerCountExpected(updatedCampaign.getInfluencerCountExpected());
            }
>>>>>>> Stashed changes
            campaignRepo.save(campaign);
            chatRoomRepository.save(chatRoom);

            return ApiResponse.sendSuccess(200, "Campaign posting updated successfully", null,
                    request.getRequestURI());

        } else {
            return ApiResponse.sendError(404, "Campaign posting not found", request.getRequestURI());
        }
    }

<<<<<<< Updated upstream
=======
    public ResponseEntity<?> updateCampaignStatus(String campaignId, StatusRequest statusRequest,
            CustomUserDetails userDetails,
            HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        Optional<Campaign> campaignOpt = campaignRepo.findByCampaignIdAndBrandId(campaignId, brandId);
        Campaign campaign = campaignOpt.get();
        campaign.setStatus(statusRequest.getStatus());
        if (statusRequest.getStatus().equals("PARTICIPATING")) {
            List<Application> applications = applicationRepository.findAllByCampaignId(campaignId);
            if (!applications.isEmpty()) {
                applications.forEach(app -> {
                    if (app.getStatus().equals("ACCEPTED")) {
                        campaignTrackingRepository
                                .save(new CampaignTracking(app.getApplicationId(), app.getCampaignId(), brandId,
                                        app.getInfluencerId(), campaign.getCampaignRequirements()));
                    } else {
                        app.setStatus("REJECTED");
                        app.setLimited(0);
                        applicationRepository.save(app);
                    }
                });
            } else {
                return ApiResponse.sendError(403, "Please confirm at least one application", request.getRequestURI());
            }
        } else if (statusRequest.getStatus().equals("COMPLETED")) {
            List<CampaignTracking> campaignTrackings = campaignTrackingRepository
                    .findAllByCampaignIdAndStatus(campaignId, "COMPLETED");
            if (campaign.getInfluencerCountCurrent() > campaignTrackings.size()) {
                return ApiResponse.sendError(403, "All campaign tracking must be completed", request.getRequestURI());
            }
        }
        campaignRepo.save(campaign);
        return ApiResponse.sendSuccess(200, "Update campaign status successfully", campaign,
                request.getRequestURI());
    }

>>>>>>> Stashed changes
    public Campaign convertToCampaign(Object campaign) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(campaign, Campaign.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert to Campaign: " + e.getMessage());
        }
    }

}
