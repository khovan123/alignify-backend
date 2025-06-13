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
import com.api.dto.request.StatusRequest;
import com.api.dto.response.CampaignResponse;
import com.api.model.Campaign;
import com.api.model.CampaignTracking;
import com.api.model.Category;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
import com.api.repository.CategoryRepository;
import com.api.security.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepo;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private CampaignTrackingRepository campaignTrackingRepository;

    public ResponseEntity<?> createCampaign(Campaign campaign, CustomUserDetails userDetails,
            HttpServletRequest request) {
        if (userDetails.getRoleId().equals(EnvConfig.BRAND_ROLE_ID)) {
            campaign.setBrandId(userDetails.getUserId());
            campaign = campaignRepo.save(campaign);
            return ApiResponse.sendSuccess(201, "Campaign posting created successfully",
                    new CampaignResponse(campaign, categoryRepo),
                    request.getRequestURI());
        }
        return ApiResponse.sendError(403, "Campaign posting only create by Brand", request.getRequestURI());
    }

    public ResponseEntity<?> getCampaignsByCampaignId(String campaignId, HttpServletRequest request) {
        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "Id: " + campaignId + " not found", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Success", new CampaignResponse(campaignOpt.get(), categoryRepo),
                request.getRequestURI());
    }

    public ResponseEntity<?> getAllCampaign(int pageNumber, int pageSize, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Campaign> campaignPage = campaignRepo.findAll(pageable);

        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(campaign -> new CampaignResponse(campaign, categoryRepo))
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
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Campaign> campaignPage = campaignRepo.findAllByBrandId(userDetails.getUserId(), pageable);
        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(campaign -> new CampaignResponse(campaign, categoryRepo))
                .toList();

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("campaigns", dtoList);
        responseData.put("currentPage", campaignPage.getNumber());
        responseData.put("totalPages", campaignPage.getTotalPages());
        responseData.put("totalItems", campaignPage.getTotalElements());

        return ApiResponse.sendSuccess(200, "Success", responseData, request.getRequestURI());
    }

    public ResponseEntity<?> getAllCampaignOfInfluencer(CustomUserDetails userDetails, int pageNumber, int pageSize,
            HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<String> campaignIdsPage = campaignTrackingRepository.findCampaignIdsByInfluencerId(userDetails.getUserId(),
                pageable);
        Page<Campaign> campaignPage = campaignRepo.findAllByCampaignIdIn(campaignIdsPage.getContent(), pageable);
        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(campaign -> new CampaignResponse(campaign, categoryRepo))
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
        Page<Campaign> campaignPage = campaignRepo.findAllByBrandId(userId, pageable);

        List<CampaignResponse> dtoList = campaignPage.getContent().stream()
                .map(campaign -> new CampaignResponse(campaign, categoryRepo))
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
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "Campaign posting not found", request.getRequestURI());
        }
        Campaign campaign = campaignOpt.get();
        if (!campaign.getBrandId().equals(userDetails.getUserId())) {
            return ApiResponse.sendError(403, "Access denied. You are not the owner of this campaign.",
                    request.getRequestURI());
        }
        List<CampaignTracking> relatedTrackings = campaignTrackingRepository.findAllByCampaignId(campaignId);
        campaignTrackingRepository.deleteAll(relatedTrackings);
        campaignRepo.deleteById(campaignId);
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
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();
            if ("COMPLETED".equals(campaign.getStatus())) {
                return ApiResponse.sendError(400, "Cannot update a campaign that is COMPLETED",
                        request.getRequestURI());
            }
            long newBudget = updatedCampaign.getBudget();
            if (newBudget < 0) {
                return ApiResponse.sendError(400, "Budget must be a non-negative number", request.getRequestURI());
            }

            if (updatedCampaign.getContent() != null) {
                campaign.setContent(updatedCampaign.getContent());
            }
            if (updatedCampaign.getImageUrl() != null) {
                campaign.setImageUrl(updatedCampaign.getImageUrl());
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
            if (updatedCampaign.getInfluencerCount() > 0) {
                campaign.setInfluencerCount(updatedCampaign.getInfluencerCount());
            }
            campaignRepo.save(campaign);

            return ApiResponse.sendSuccess(200, "Campaign posting updated successfully",
                    new CampaignResponse(campaign, categoryRepo),
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
        campaign.setStatus(statusRequest.getStatus());
        campaignRepo.save(campaign);
        return ApiResponse.sendSuccess(200, "Update campaign status successfully", campaign, request.getRequestURI());
    }

    public Campaign convertToCampaign(Object campaign) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(campaign, Campaign.class);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Failed to convert to Campaign: " + e.getMessage());
        }
    }

}
