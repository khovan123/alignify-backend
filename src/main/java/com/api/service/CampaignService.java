package com.api.service;

import com.api.dto.ApiResponse;
import com.api.dto.CampaignResponse;
import com.api.model.Campaign;
import com.api.model.Category;
import com.api.repository.CampaignRepository;
import com.api.repository.CategoryRepository;
import com.api.util.Helper;
import jakarta.servlet.http.HttpServletRequest;
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

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepo;
    @Autowired
    private CategoryRepository categoryRepo;

    public ResponseEntity<?> createCampaign(Campaign campaign, HttpServletRequest request) {
        campaign = campaignRepo.save(campaign);
        return ApiResponse.sendSuccess(201, "Campaign posting created successfully", campaign, request.getRequestURI());
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
        dto.setTimestamp(post.getTimestamp());
        dto.setIsPublic(post.isIsPublic());
        dto.setStatus(post.getStatus());

        return dto;
    }

    public ResponseEntity<?> getAllCampaign(int pageNumber, int pageSize, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "timestamp"));
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

    public ResponseEntity<?> getCampaignsById(String userId, int pageNumber, int pageSize, HttpServletRequest request) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "timestamp"));
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

    public ResponseEntity<?> deleteCampaign(String campaignId, String userId, HttpServletRequest request) {
        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
        if (campaignOpt.isPresent()) {
            Campaign campaign = campaignOpt.get();
            if (!campaign.getUserId().equals(userId)) {
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

    public ResponseEntity<?> updateCampaign(String campaignId, String user,
            Campaign updatedCampaign,
            HttpServletRequest request) {

        Optional<Campaign> campaignOpt = campaignRepo.findById(campaignId);
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

            List<String> validCategoryIds = validCategories.stream()
                    .map(Category::getCategoryId)
                    .toList();

            campaign.setContent(updatedCampaign.getContent());
            campaign.setImageUrl(updatedCampaign.getImageUrl());
            campaign.setCategoryIds(validCategoryIds);
            campaign.setIsPublic(updatedCampaign.isIsPublic());
            campaign.setStatus(newStatus);

            campaignRepo.save(campaign);

            return ApiResponse.sendSuccess(200, "Campaign posting updated successfully", null,
                    request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "Campaign posting not found", request.getRequestURI());
        }
    }
}
