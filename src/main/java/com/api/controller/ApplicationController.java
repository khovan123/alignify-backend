package com.api.controller;

import com.api.security.CustomUserDetails;
import com.api.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaigns")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/{campaignId}/applications/apply")
<<<<<<< Updated upstream
    @PreAuthorize("hasRole('ROLE_INFLUENCER')")
    public ResponseEntity<?> apply(@PathVariable("campaignId") String campaignId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getUserId();
        return applicationService.apply_Application(userId, campaignId, request);
=======
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and (@securityService.checkCampaignStatus(#campaignId,'PENDING',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'DRAFT',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
    public ResponseEntity<?> apply(
            @PathVariable("campaignId") String campaignId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return applicationService.apply_Application(campaignId, userDetails, request);
>>>>>>> Stashed changes
    }

    @PostMapping("/applications/{applicationId}/cancel")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isApplicationOwner(#applicationId, authentication.principal.userId)")
    public ResponseEntity<?> cancel(@PathVariable("applicationId") String applicationId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getUserId();
        return applicationService.cancel_Application(userId, applicationId, request);
    }

    @PostMapping("/applications/{applicationId}/re-apply")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isApplicationOwner(#applicationId, authentication.principal.userId)")
    public ResponseEntity<?> reApply(@PathVariable("applicationId") String applicationId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getUserId();
        return applicationService.reApply_Application(userId, applicationId, request);
    }

    @PostMapping("/applications/{applicationId}/confirm")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isApplicationReceiver(#campaignId, authentication.principal.userId)")
    public ResponseEntity<?> confirm(@PathVariable("applicationId") String applicationId, @RequestParam("accepted") Boolean accepted, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getUserId();
        return applicationService.confirm_Application(userId, applicationId, accepted, request);
    }
}
