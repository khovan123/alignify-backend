package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.security.CustomUserDetails;
import com.api.service.ApplicationService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/campaigns")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/applications/brand")
    @PreAuthorize("hasRole('ROLE_BRAND')")
    public ResponseEntity<?> getAllApplicationByBrand(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return applicationService.getAllApplicationByBrand(userDetails, request);
    }

    @GetMapping("/applications/influencer")
    @PreAuthorize("hasRole('ROLE_INFLUENCER')")
    public ResponseEntity<?> getAllApplicationByInfluencer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return applicationService.getAllApplicationByInfluencer(userDetails, request);
    }

    @PostMapping("/{campaignId}/applications/apply")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and (@securityService.checkCampaignStatus(#campaignId,'PENDING',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'DRAFT',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
    public ResponseEntity<?> apply(
            @PathVariable("campaignId") String campaignId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return applicationService.apply_Application(campaignId, userDetails, request);
    }

    @PostMapping("/applications/{applicationId}/cancel")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isApplicationOwner(#applicationId, authentication.principal)")
    public ResponseEntity<?> cancel(
            @PathVariable("applicationId") String applicationId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return applicationService.cancel_Application(applicationId, userDetails, request);
    }

    @PostMapping("/applications/{applicationId}/re-apply")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isApplicationOwner(#applicationId, authentication.principal)")
    public ResponseEntity<?> reApply(
            @PathVariable("applicationId") String applicationId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return applicationService.reApply_Application(applicationId, userDetails, request);
    }

    @PostMapping("/applications/{applicationId}/confirm")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isApplicationReceiver(#applicationId, authentication.principal)")
    public ResponseEntity<?> confirm(
            @PathVariable("applicationId") String applicationId,
            @RequestParam("accepted") Boolean accepted,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return applicationService.confirm_Application(applicationId, accepted, userDetails, request);
    }
}