package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.api.dto.ApiResponse;
import com.api.dto.request.StatusRequest;
import com.api.model.Campaign;
import com.api.security.CustomUserDetails;
import com.api.service.CampaignService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/v1/campaigns")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @GetMapping("/{campaignId}")
    public ResponseEntity<?> getCampaign(
            @PathVariable("campaignId") String campaignId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return campaignService.getCampaignsByCampaignId(campaignId, request);
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCampaigns(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        return campaignService.getAllCampaign(page, size, request);
    }

    // @GetMapping("/brands/{brandId}")
    // public ResponseEntity<?> getCampaignsByBrandId(
    // @PathVariable("brandId") String userId,
    // @RequestParam(defaultValue = "0") int page,
    // @RequestParam(defaultValue = "10") int size,
    // HttpServletRequest request) {
    // return campaignService.getCampaignsByUserId(userId, page, size, request);
    // }

    // --update thêm file ảnh--vao request
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_BRAND')")
    public ResponseEntity<?> createCampaign(
            @RequestPart("campaign") String obj,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        System.out.println(obj);
        try {
            System.out.println("Received campaign JSON: " + obj);
            Campaign campaign = campaignService.convertToCampaign(obj);
            if (image != null && !image.isEmpty()) {
                System.out.println("Received image: " + image.getOriginalFilename());
            } else {
                System.out.println("No image provided");
            }
            return campaignService.createCampaign(campaign, image, userDetails, request);
        } catch (Exception e) {
            return ApiResponse.sendError(400, "Invalid request: " + e.getMessage(), request.getRequestURI());
        }
    }

    @PreAuthorize("hasRole('ROLE_BRAND')")
    @GetMapping("/brand")
    public ResponseEntity<?> getAllCampaignOfBrand(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        return campaignService.getAllCampaignOfBrand(userDetails, page, size, request);
    }

    @PutMapping("/{campaignId}")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isCampaignOwner(#campaignId, authentication.principal) and (@securityService.checkCampaignStatus(#campaignId,'PENDING',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'DRAFT',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
    public ResponseEntity<?> updateCampaign(@PathVariable String campaignId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody Campaign campaign,
            HttpServletRequest request) {
        return campaignService.updateCampaign(campaignId, userDetails, campaign, request);
    }

    @PutMapping("/{campaignId}/status")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isCampaignOwner(#campaignId, authentication.principal)")
    public ResponseEntity<?> updateCampaignStatus(
            @PathVariable String campaignId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody StatusRequest statusRequest,
            HttpServletRequest request) {
        return campaignService.updateCampaignStatus(campaignId, statusRequest, userDetails, request);
    }

    @DeleteMapping("/{campaignId}")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isCampaignOwner(#campaignId, authentication.principal) and (@securityService.checkCampaignStatus(#campaignId,'PENDING',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'DRAFT',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
    public ResponseEntity<?> deleteCampaign(
            @PathVariable String campaignId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {

        return campaignService.deleteCampaign(campaignId, userDetails, request);
    }

    @PreAuthorize("hasRole('ROLE_INFLUENCER')")
    @GetMapping("/influencer")
    public ResponseEntity<?> getAllCampaignOfInfluencer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        return campaignService.getAllCampaignOfInfluencer(userDetails, page, size, request);
    }

}
