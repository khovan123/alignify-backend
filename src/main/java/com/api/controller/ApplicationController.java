package com.api.controller;

import com.api.security.CustomUserDetails;
import com.api.service.ApplicationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/campaigns")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/{campaignId}/applications/apply")
    public ResponseEntity<?> apply(@PathVariable("campaignId") String campaignId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getId();
        return applicationService.apply_Application(userId, campaignId, request);
    }

    @PostMapping("/applications/{applicationId}/cancel")
    public ResponseEntity<?> cancel(@PathVariable("applicationId") String applicationId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getId();
        return applicationService.cancel_Application(userId, applicationId, request);
    }

    @PostMapping("/applications/{applicationId}/re-apply")
    public ResponseEntity<?> reApply(@PathVariable("applicationId") String applicationId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getId();
        return applicationService.reApply_Application(userId, applicationId, request);
    }

    @PostMapping("/applications/{applicationId}/confirm")
    public ResponseEntity<?> confirm(@PathVariable("applicationId") String applicationId, @RequestParam("accepted") Boolean accepted, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getId();
        return applicationService.confirm_Application(userId, applicationId, accepted, request);
    }
}
