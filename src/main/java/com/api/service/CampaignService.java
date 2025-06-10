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

import com.api.config.EnvConfig;
import com.api.dto.ApiResponse;
import com.api.dto.CampaignResponse;
import com.api.model.Campaign;
import com.api.model.Category;
import com.api.model.User;
import com.api.repository.CampaignRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import com.api.util.Helper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<?> createCampaign(Campaign campaign,CustomUserDetails userDetails, HttpServletRequest request) {
        System.out.println(userDetails.getRoleId());
        System.out.println(EnvConfig.BRAND_ROLE_ID);
        if(userDetails.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)){
        campaign.setUserId(userDetails.getId());
        campaign = campaignRepo.save(campaign);
        return ApiResponse.sendSuccess(201, "Campaign posting created successfully", campaign, request.getRequestURI());
        }
        return ApiResponse.sendError(403,"Campaign posting only create by Brand", request.getRequestURI());
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
        dto.setPublic(post.isPublic());
        dto.setStatus(post.getStatus());
        dto.setBudget(post.getBudget());
        dto.setCampaignRequirements(post.getCampaignRequirements());
        dto.setInfluencerRequirement(post.getInfluencerRequirement());

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

    public ResponseEntity<?> getMe(CustomUserDetails userDetails, int pageNumber, int pageSize, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Campaign> campaignPage = campaignRepo.findByUserId(userDetails.getId(), pageable);
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
    public ResponseEntity<?> getCampaignsByUserId(String userId, int pageNumber, int pageSize, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Campaign> campaignPage = campaignRepo.findByUserIdAndIsPublic(userId,true, pageable);

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

    public ResponseEntity<?> deleteCampaign(String campaignId, CustomUserDetails userDetails, HttpServletRequest request) {
        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();
            if (!campaign.getUserId().equals(userDetails.getId())) {
                return ResponseEntity.status(403).body(
                        Map.of("error", "Access denied. You are not the owner of this content."));
            }
            campaignRepo.deleteById(campaignId);
            return ApiResponse.sendSuccess(
                    204,
                    "campaign posting deleted successfully",
                    null,
                    request.getRequestURI()
            );
        } else {
            return ApiResponse.sendError(
                    404,
                    "Campaign posting not found",
                    request.getRequestURI()
            );
        }
    }

    public ResponseEntity<?> updateCampaign(String campaignId, CustomUserDetails userDetails,
            Campaign updatedCampaign,
            HttpServletRequest request) {

        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        User user = userRepository.findById(userDetails.getId()).get();
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();

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
                return ApiResponse.sendError(400, "Invalid status. Allowed values: DRAFT, PENDING, COMPLETED", request.getRequestURI());
            }
            long newBudget = updatedCampaign.getBudget();
            if (newBudget < 0) {
                return ApiResponse.sendError(400, "Budget must be a non-negative number", request.getRequestURI());
            }
            List<String> validCategoryIds = validCategories.stream()
                    .map(Category::getCategoryId)
                    .toList();

            campaign.setContent(updatedCampaign.getContent());
            campaign.setImageUrl(updatedCampaign.getImageUrl());
            campaign.setCategoryIds(validCategoryIds);
            campaign.setPublic(updatedCampaign.isPublic());
            campaign.setStatus(newStatus);
            campaign.setBudget(newBudget);
            campaign.setCampaignRequirements(updatedCampaign.getCampaignRequirements());
            campaign.setInfluencerRequirement(updatedCampaign.getInfluencerRequirement());

            campaignRepo.save(campaign);

            return ApiResponse.sendSuccess(200, "Campaign posting updated successfully", null,
                    request.getRequestURI());
            
        } else {
            return ApiResponse.sendError(404, "Campaign posting not found", request.getRequestURI());
        }
    }

}
