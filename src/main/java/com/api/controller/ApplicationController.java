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

    @GetMapping("/applications/me")
    @PreAuthorize("hasRole('ROLE_BRAND')")
    public ResponseEntity<?> getAllApplicationByMe(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        return applicationService.getAllApplicationByMe(pageNumber, pageSize, userDetails, request);
    }

    @PostMapping("/{campaignId}/applications/apply")
    @PreAuthorize("hasRole('ROLE_INFLUENCER')")
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
