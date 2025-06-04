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

    return dto;
}
    public ResponseEntity<?> getAllCampaign(HttpServletRequest request) {
        List<Campaign> campaignList = campaignRepo.findAll();
        List<CampaignResponse> dtoList = campaignList.stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
    }

    public ResponseEntity<?> getCampaignsById(String userId, HttpServletRequest request) {
        List<Campaign> campaignList = campaignRepo.findByUserId(userId);
        List<CampaignResponse> dtoList = campaignList.stream()
                .map(this::mapToDTO)
                .toList();
        return ApiResponse.sendSuccess(200, "Success", dtoList, request.getRequestURI());
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
                    200,
                    "campaign posting deleted successfully",
                    null,
                    request.getRequestURI()
            );
        } else {
            return ApiResponse.sendError(
                    404,
                    "Content posting not found",
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

            List<String> validCategoryIds = validCategories.stream()
                    .map(Category::getCategoryId)
                    .toList();

            campaign.setContent(updatedCampaign.getContent());
            campaign.setImageUrl(updatedCampaign.getImageUrl());
            campaign.setCategoryIds(validCategoryIds);
            campaign.setIsPublic(updatedCampaign.isIsPublic());

            campaignRepo.save(campaign);

            return ApiResponse.sendSuccess(200, "Content posting updated successfully", campaign,
                    request.getRequestURI());
        } else {
            return ApiResponse.sendError(404, "Content posting not found", request.getRequestURI());
        }
    }

}
