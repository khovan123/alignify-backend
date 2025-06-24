package com.api.service;

import com.api.config.EnvConfig;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.api.dto.ApiResponse;
import com.api.dto.request.StatusRequest;
import com.api.dto.response.CampaignResponse;
import com.api.model.Application;
import com.api.model.Brand;
import com.api.model.Campaign;
import com.api.model.CampaignTracking;
import com.api.model.Category;
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.model.User;
import com.api.repository.ApplicationRepository;
import com.api.repository.BrandRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CampaignService {

    private static final Logger logger = LoggerFactory.getLogger(CampaignService.class);

    @Autowired
    private CampaignRepository campaignRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private CampaignTrackingRepository campaignTrackingRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private BrandRepository brandRepository;

    public ResponseEntity<?> createCampaign(Campaign campaign, MultipartFile file, CustomUserDetails userDetails,
            HttpServletRequest request) {

        String brandId = userDetails.getUserId();
        if (!(campaign.getStatus().equals("DRAFT") || campaign.getStatus().equals("RECRUITING"))) {
            ApiResponse.sendError(400, "Illegal status", request.getRequestURI());
        }
        validateCampaignRequirements(campaign.getCampaignRequirements());
        String imageUrl;
        try {
            imageUrl = fileStorageService.storeFile(file);
        } catch (Exception e) {
            return ApiResponse.sendError(500, e.getMessage(), request.getRequestURI());
        }
        campaign.setBrandId(brandId);
        campaign.setImageUrl(imageUrl);
        campaign = campaignRepo.save(campaign);
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("Brand not found"));
        brand.setTotalCampaign(brand.getTotalCampaign() + 1);
        brandRepository.save(brand);

        chatRoomRepository.save(
                new ChatRoom(campaign.getCampaignId(), brandId, campaign.getCampaignName(), campaign.getImageUrl()));
        User user = userRepository.findById(brandId).get();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage("Xin chào " + user.getName() + " !");
        chatMessage.setChatRoomId(campaign.getCampaignId());
        chatMessage.setName(user.getName());
        chatMessage.setReadBy(new ArrayList<>(Arrays.asList(brandId)));
        chatMessage.setUserId("#SYS");
        chatMessage.setSendAt(LocalDateTime.now());
        chatMessageRepository.save(chatMessage);
        return ApiResponse.sendSuccess(201, "Campaign posting created successfully",
                new CampaignResponse(user, campaign, categoryRepo),
                request.getRequestURI());
    }

    private void validateCampaignRequirements(Map<String, Integer> campaignRequirements) {
        if (campaignRequirements != null) {
            for (Map.Entry<String, Integer> entry : campaignRequirements.entrySet()) {
                if (entry.getValue() == null || entry.getValue() < 0) {
                    throw new IllegalArgumentException("Campaign requirements must be non-negative integers");
                }
            }
        }
    }

    public ResponseEntity<?> getCampaignsByCategoryIds(
            int pageNumber,
            int pageSize,
            String categoryIds,
            HttpServletRequest request) {

        if (categoryIds == null || categoryIds.isEmpty()) {
            return ApiResponse.sendError(400, "Category list must not be empty", request.getRequestURI());
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Campaign> campaignPage = campaignRepo.findByCategoryIdsInAndStatusOrderByCreatedAtDesc(
                categoryIds, "RECRUITING", pageable);

        Set<String> brandIds = campaignPage.getContent().stream()
                .map(Campaign::getBrandId)
                .collect(Collectors.toSet());

        Map<String, User> brandMap = userRepository.findAllById(brandIds).stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(campaign -> {
                    User user = brandMap.get(campaign.getBrandId());
                    if (user == null) {
                        throw new IllegalArgumentException(
                                "Brand user not found for campaign " + campaign.getCampaignId());
                    }
                    return new CampaignResponse(user, campaign, categoryRepo);
                })
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", campaignPage.getNumber());
        responseData.put("totalPages", campaignPage.getTotalPages());
        responseData.put("totalItems", campaignPage.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> getCampaignsTop(HttpServletRequest request) {

        List<Campaign> campaigns = campaignRepo.findTop3ByOrderByApplicationTotalDescCreatedAtDesc();

        Set<String> brandIds = campaigns.stream()
                .map(Campaign::getBrandId)
                .collect(Collectors.toSet());

        Map<String, User> brandMap = userRepository.findAllById(brandIds).stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        List<CampaignResponse> dtoList = campaigns.stream()
                .map(campaign -> {
                    User user = brandMap.get(campaign.getBrandId());
                    if (user == null) {
                        throw new IllegalArgumentException(
                                "Brand user not found for campaign " + campaign.getCampaignId());
                    }
                    return new CampaignResponse(user, campaign, categoryRepo);
                })
                .toList();

        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> getCampaignsByCampaignId(String campaignId, HttpServletRequest request) {
        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        User user = userRepository.findById(campaignOpt.get().getBrandId()).get();
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "Id: " + campaignId + " not found", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Success", new CampaignResponse(user, campaignOpt.get(), categoryRepo),
                request.getRequestURI());
    }

    public ResponseEntity<?> getAllCampaign(int pageNumber, int pageSize, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Campaign> campaignPage = campaignRepo.findAllByStatusOrderByCreatedAtDesc("RECRUITING", pageable);
        Set<String> brandIds = campaignPage.getContent().stream()
                .map(Campaign::getBrandId)
                .collect(Collectors.toSet());

        Map<String, User> brandMap = userRepository.findAllById(brandIds).stream()
                .collect(Collectors.toMap(User::getUserId, Function.identity()));

        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(campaign -> {
                    User user = brandMap.get(campaign.getBrandId());
                    return new CampaignResponse(user, campaign, categoryRepo);
                })
                .toList();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", campaignPage.getNumber());
        responseData.put("totalPages", campaignPage.getTotalPages());
        responseData.put("totalItems", campaignPage.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> getAllCampaignOfBrand(CustomUserDetails userDetails, int pageNumber, int pageSize,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Campaign> campaignPage = campaignRepo.findAllByBrandId(userDetails.getUserId(), pageable);
        User brandUser = userRepository.findById(userDetails.getUserId()).orElse(null);

        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(campaign -> new CampaignResponse(brandUser, campaign, categoryRepo))
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", campaignPage.getNumber());
        responseData.put("totalPages", campaignPage.getTotalPages());
        responseData.put("totalItems", campaignPage.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> getAllCampaignOfBrandNoPage(CustomUserDetails userDetails, HttpServletRequest request) {

        List<Campaign> campaigns = campaignRepo.findAllByBrandId(userDetails.getUserId());
        User brandUser = userRepository.findById(userDetails.getUserId()).orElse(null);

        List<CampaignResponse> dtoList = campaigns.stream()
                .map(campaign -> new CampaignResponse(brandUser, campaign, categoryRepo))
                .toList();
        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> getAllCampaignOfInfluencer(CustomUserDetails userDetails, int pageNumber, int pageSize,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<String> campaignIdsPage = campaignTrackingRepository.findCampaignIdsByInfluencerId(userDetails.getUserId(),
                pageable);
        Optional<User> user = userRepository.findById(userDetails.getUserId());
        Page<Campaign> campaignPage = campaignRepo.findAllByCampaignIdIn(campaignIdsPage.getContent(), pageable);
        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(campaign -> new CampaignResponse(user.get(), campaign, categoryRepo))
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", campaignPage.getNumber());
        responseData.put("totalPages", campaignPage.getTotalPages());
        responseData.put("totalItems", campaignPage.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    // public ResponseEntity<?> getCampaignsByUserId(String userId, int pageNumber,
    // int pageSize,
    // HttpServletRequest request) {
    // Pageable pageable = PageRequest.of(pageNumber, pageSize,
    // Sort.by(Sort.Direction.DESC, "createdDate"));
    // Page<Campaign> campaignPage = campaignRepo.findAllByBrandId(userId,
    // pageable);
    // Optional<User> user = userRepository.findById(userId);
    // List<CampaignResponse> dtoList = campaignPage.getContent().stream()
    // .map(campaign -> new CampaignResponse(user.get(), campaign, categoryRepo))
    // .toList();
    // Map<String, Object> responseData = new HashMap<>();
    // responseData.put("campaigns", dtoList);
    // responseData.put("currentPage", campaignPage.getNumber());
    // responseData.put("totalPages", campaignPage.getTotalPages());
    // responseData.put("totalItems", campaignPage.getTotalElements());
    // return ApiResponse.sendSuccess(200, "Success", responseData,
    // request.getRequestURI());
    // }
    public ResponseEntity<?> deleteCampaign(String campaignId, CustomUserDetails userDetails,
            HttpServletRequest request) {
        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "Campaign posting not found", request.getRequestURI());
        }
        Campaign campaign = campaignOpt.get();
        if (!campaign.getBrandId().equals(userDetails.getUserId())) {
            return ApiResponse.sendError(403, "Access denied. You are not the owner of this campaign.",
                    request.getRequestURI());
        }
        if (!campaign.getStatus().equals("DRAFT")) {
            return ApiResponse.sendError(403, "Access denied",
                    request.getRequestURI());
        }
        List<CampaignTracking> relatedTrackings = campaignTrackingRepository.findAllByCampaignId(campaignId);
        campaignTrackingRepository.deleteAll(relatedTrackings);
        campaignRepo.deleteById(campaignId);
        chatRoomRepository.deleteById(campaignId);
        return ApiResponse.sendSuccess(
                204,
                "campaign posting and related trackings deleted successfully",
                null,
                request.getRequestURI());
    }

    public ResponseEntity<?> updateCampaign(String campaignId, CustomUserDetails userDetails,
            Campaign updatedCampaign,
            HttpServletRequest request) {

        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        Optional<User> user = userRepository.findById(campaignOpt.get().getBrandId());
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();
            ChatRoom chatRoom = chatRoomRepository.findById(campaignId).get();
            if (!"DRAFT".equals(campaign.getStatus())) {
                return ApiResponse.sendError(400, "Access denied.",
                        request.getRequestURI());
            }
            int newBudget = updatedCampaign.getBudget();
            if (newBudget < 0) {
                return ApiResponse.sendError(400, "Budget must be a non-negative number", request.getRequestURI());
            }
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
            if (updatedCampaign.getStartAt() != null) {
                campaign.setStartAt(updatedCampaign.getStartAt());
            }
            if (updatedCampaign.getDueAt() != null) {
                campaign.setDueAt(updatedCampaign.getDueAt());
            }
            if (updatedCampaign.getCampaignRequirements() != null
                    && !updatedCampaign.getCampaignRequirements().isEmpty()) {
                campaign.setCampaignRequirements(updatedCampaign.getCampaignRequirements());
            }
            if (updatedCampaign.getInfluencerRequirements() != null
                    && !updatedCampaign.getInfluencerRequirements().isEmpty()) {
                campaign.setInfluencerRequirements(updatedCampaign.getInfluencerRequirements());
            }
            if (updatedCampaign.getInfluencerCountExpected() > 0) {
                campaign.setInfluencerCountExpected(updatedCampaign.getInfluencerCountExpected());
            }
            campaign.setCreatedAt(LocalDateTime.now(TimeZone.getTimeZone("Asia/Ho_Chi_Minh").toZoneId()));
            campaignRepo.save(campaign);
            chatRoomRepository.save(chatRoom);

            return ApiResponse.sendSuccess(200, "Campaign posting updated successfully",
                    new CampaignResponse(user.get(), campaign, categoryRepo),
                    request.getRequestURI());

        } else {
            return ApiResponse.sendError(404, "Campaign posting not found", request.getRequestURI());
        }
    }

    public ResponseEntity<?> updateCampaignStatus(String campaignId, StatusRequest statusRequest,
            CustomUserDetails userDetails,
            HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        Optional<Campaign> campaignOpt = campaignRepo.findByCampaignIdAndBrandId(campaignId, brandId);
        Campaign campaign = campaignOpt.get();
        if ((!campaign.getStatus().equals("PENDING")) && (!campaign.getStatus().equals("RECRUITING"))
                && statusRequest.getStatus().equals("DRAFT")) {
            return ApiResponse.sendError(403, "Access denied.", request.getRequestURI());
        }
        campaign.setStatus(statusRequest.getStatus());
        if (campaign.getStatus().equals("DRAFT") && statusRequest.getStatus().equals("RECRUITING")) {
        } else if (campaign.getStatus().equals("RECRUITING") && statusRequest.getStatus().equals("PENDING")) {
        } else if ((campaign.getStatus().equals("PENDING")) || (campaign.getStatus().equals("RECRUITING"))
                && statusRequest.getStatus().equals("DRAFT")) {
            applicationRepository.deleteAllByCampaignId(campaignId);
            ChatRoom chatRoom = chatRoomRepository.findById(campaignId).get();
            chatRoom.setMembers(new ArrayList<>(Arrays.asList(brandId)));
            chatRoomRepository.save(chatRoom);
            chatMessageRepository.deleteAllByChatRoomId(campaignId);
        } else if (statusRequest.getStatus().equals("PARTICIPATING") && campaign.getStatus().equals("PENDING")) {
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
        } else if (statusRequest.getStatus().equals("COMPLETED") && campaign.getStatus().equals("PARTICIPATING")) {
            List<CampaignTracking> campaignTrackings = campaignTrackingRepository
                    .findAllByCampaignIdAndStatus(campaignId, "COMPLETED");
            if (campaign.getInfluencerCountCurrent() > campaignTrackings.size()) {
                return ApiResponse.sendError(403, "All campaign tracking must be completed", request.getRequestURI());
            }
        } else {
            return ApiResponse.sendError(403, "Not supported yet.", request.getRequestURI());
        }
        campaignRepo.save(campaign);
        return ApiResponse.sendSuccess(200, "Update campaign status successfully", campaign,
                request.getRequestURI());
    }

    // public Campaign convertToCampaign(Object campaign) {
    // ObjectMapper mapper = new ObjectMapper();
    // try {
    // return mapper.convertValue(campaign, Campaign.class);
    // } catch (IllegalArgumentException e) {
    // throw new IllegalArgumentException("Failed to convert to Campaign: " +
    // e.getMessage());
    // }
    // }
    
    public ResponseEntity<?> searchByTerm(String term, int pageNumber, int pageSize, CustomUserDetails userDetails, HttpServletRequest request) {
        if (term.isBlank() || term.isEmpty()) {
            return this.getAllCampaign(pageNumber, pageSize, request);
        }
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<User> matchedBrands = userRepository.findByNameContainingIgnoreCaseAndRoleId(term, EnvConfig.BRAND_ROLE_ID);
        List<String> matchedBrandIds = matchedBrands.stream()
                .map(User::getUserId)
                .toList();

        Page<Campaign> matchedCampaigns;

        if (!matchedBrandIds.isEmpty()) {
            matchedCampaigns = campaignRepo.findByBrandIdIn(matchedBrandIds, pageable);
        } else {
            matchedCampaigns = campaignRepo.findByCampaignNameContainingIgnoreCase(term, pageable);
        }
        if( matchedCampaigns.isEmpty()) {
            return ApiResponse.sendSuccess(200, "No campaigns found", Collections.emptyList(), request.getRequestURI());
        }
        List<CampaignResponse> dtoList = matchedCampaigns.getContent().stream()
                .map(campaign -> {
                    User brand = userRepository.findById(campaign.getBrandId())
                            .orElseThrow(() -> new IllegalArgumentException("Brand not found for campaign: " + campaign.getCampaignId()));
                    return new CampaignResponse(brand, campaign, categoryRepo);
                })
                .toList();

        return ApiResponse.sendSuccess(200, "Response success", dtoList, request.getRequestURI());
    }

    public Campaign convertToCampaign(String obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.readValue(obj, Campaign.class);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid campaign JSON: " + e.getMessage(), e);
        }
    }
}
