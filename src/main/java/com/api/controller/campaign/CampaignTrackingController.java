package com.api.controller.campaign;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.api.dto.request.PostDetailsTracking;
import com.api.model.PlatformRequirementDetailsTracking;
import com.api.security.CustomUserDetails;
import com.api.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.api.dto.ApiResponse;
import com.api.dto.request.PlatformRequirementDetailsTrackingRequest;
import com.api.model.CampaignTracking;
import com.api.model.PlatformRequirementTracking;
import com.api.repository.CampaignTrackingRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/campaigns/{campaignId}/trackings")
public class CampaignTrackingController {

    @Autowired
    private CampaignTrackingRepository campaignTrackingRepository;
    @Autowired
    private SecurityService securityService;

    @GetMapping("/influencer")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isJoinedCampaignTracking(#campaignId, authentication.principal)")
    public ResponseEntity<?> getCampaignTrackingByInfluencer(@PathVariable("campaignId") String campaignId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        String userId = userDetails.getUserId();
        Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository.findByCampaignIdAndInfluencerId(campaignId, userId);
        if (!campaignTrackingOpt.isPresent()) {
            return ApiResponse.sendError(404, "Not found!", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Reponse successfully", campaignTrackingOpt.get(), request.getRequestURI());
    }

    @GetMapping("/brand")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isJoinedCampaignTracking(#campaignId, authentication.principal)")
    public ResponseEntity<?> getCampaignTrackingByBrand(@PathVariable("campaignId") String campaignId, @AuthenticationPrincipal CustomUserDetails userDetails, HttpServletRequest request) {
        List<CampaignTracking> campaignTrackings = campaignTrackingRepository.findAllByCampaignId(campaignId);
        return ApiResponse.sendSuccess(200, "Reponse successfully", campaignTrackings, request.getRequestURI());
    }

    @PostMapping("/{trackingId}/posts")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isJoinedCampaignTracking(#campaignId, authentication.principal)")
    public ResponseEntity<?> addContentRequirement(@PathVariable("campaignId") String campaignId, @PathVariable("trackingId") String trackingId, @RequestBody PlatformRequirementDetailsTrackingRequest platformRequirementDetailsTrackingRequest, HttpServletRequest request) {
        try {
            Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository.findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
            if (campaignTrackingOpt.isEmpty()) {
                return ApiResponse.sendError(404, "Tracking ID: " + trackingId + " not found", request.getRequestURI());
            }
            CampaignTracking campaignTracking = campaignTrackingOpt.get();
            List<PlatformRequirementTracking> platformRequirementTrackings = campaignTracking.getPlatformRequirementTracking();
            for (PlatformRequirementTracking tracking : platformRequirementTrackings) {
                tracking.getDetails().removeIf(detail -> detail.getPostUrl() == null || detail.getPostUrl().isEmpty());
            }
            List<PostDetailsTracking> postDetailsTrackings = platformRequirementDetailsTrackingRequest.getPostDetailsTrackings();
            for (PostDetailsTracking postDetailsTracking : postDetailsTrackings) {
                for (PlatformRequirementTracking platformRequirementTracking : platformRequirementTrackings) {
                    if (platformRequirementTracking.getPlatform().equals(postDetailsTracking.getPlatform()) && platformRequirementTracking.getPost_type().equals(postDetailsTracking.getPost_type())) {

                        List<PlatformRequirementDetailsTracking> details = platformRequirementTracking.getDetails();
                        int idx = postDetailsTracking.getIndex();
                        while (details.size() <= idx) {
                            details.add(new PlatformRequirementDetailsTracking());
                        }
                        details.set(idx, new PlatformRequirementDetailsTracking(postDetailsTracking));
                    }
                }
            }
            if (platformRequirementTrackings.isEmpty()) {
                platformRequirementTrackings = new ArrayList<>();
            }
            campaignTracking.setPlatformRequirementTracking(platformRequirementTrackings);
            campaignTrackingRepository.save(campaignTracking);
            return ApiResponse.sendSuccess(200, "Response successfully", null, request.getRequestURI());

        } catch (Exception e) {
            return ApiResponse.sendError(500, "Internal server error: " + e.getMessage(), request.getRequestURI());
        }
    }


    @PutMapping("/{trackingId}/posts/confirm")
    @PreAuthorize("hasRole('ROLE_BRAND') && @securityService.isJoinedCampaignTracking(#campaignId, authentication.principal) && @securityService.checkCampaignStatus(#campaignId, 'PARTICIPATING',authentication.principal)")
    public ResponseEntity<?> confirmRequirementStatus(@PathVariable("campaignId") String campaignId, @PathVariable("trackingId") String trackingId, @RequestParam("accepted") boolean accepted, @RequestBody PostDetailsTracking postDetailsTracking, HttpServletRequest request) {
        Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository.findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
        if (!campaignTrackingOpt.isPresent()) {
            return ApiResponse.sendError(404, "Id: " + trackingId + " not found!", request.getRequestURI());
        }
        CampaignTracking tracking = campaignTrackingOpt.get();
        List<PlatformRequirementTracking> platformRequirementTrackings = tracking.getPlatformRequirementTracking();
        for (PlatformRequirementTracking platformRequirementTracking : platformRequirementTrackings) {
            if (platformRequirementTracking.getPlatform().equals(postDetailsTracking.getPlatform()) && platformRequirementTracking.getPost_type().equals(postDetailsTracking.getPost_type()) && platformRequirementTracking.getDetails().get(postDetailsTracking.getIndex()).getPostUrl().equals(postDetailsTracking.getPostUrl())) {
                platformRequirementTracking.getDetails().get(postDetailsTracking.getIndex()).setStatus(accepted ? "ACCEPTED" : "REJECTED");
            }
        }

        double process = calculateProcess(tracking.getPlatformRequirementTracking());
        tracking.setProcess(process);
        if (process >= 100.0) {
            tracking.setStatus("COMPLETED");
        }
        tracking.setPlatformRequirementTracking(platformRequirementTrackings);
        campaignTrackingRepository.save(tracking);
        return ApiResponse.sendSuccess(200, "Update status of campaign requirement successfully", null, request.getRequestURI());
    }

    private double calculateProcess(List<PlatformRequirementTracking> platformRequirementTracking) {
        int totalContents = platformRequirementTracking.stream().mapToInt(p -> p.getDetails() != null ? p.getDetails().size() : 0).sum();
        int acceptedContents = platformRequirementTracking.stream().flatMap(p -> p.getDetails() != null ? p.getDetails().stream() : java.util.stream.Stream.empty()).mapToInt(content -> {
            String status = content.getStatus();
            if (status == null) {
                return 0;
            }
            return status.equalsIgnoreCase("ACCEPTED") ? 1 : 0;
        }).sum();
        return totalContents > 0 ? (acceptedContents * 100.0 / totalContents) : 0.0;
    }

}
