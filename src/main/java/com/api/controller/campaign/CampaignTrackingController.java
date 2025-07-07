package com.api.controller.campaign;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.ApiResponse;
import com.api.dto.request.PlatformRequirementDetailsTrackingRequest;
import com.api.model.CampaignTracking;
import com.api.model.PlatformRequirementTracking;
import com.api.repository.CampaignTrackingRepository;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/campaigns/{campaignId}/trackings/{trackingId}")
public class CampaignTrackingController {

    @Autowired
    private CampaignTrackingRepository campaignTrackingRepository;

    @GetMapping("")
    @PreAuthorize("hasAnyRole('ROLE_BRAND', 'ROLE_INFLUENCER') and @securityService.isJoinedCampaignTracking(#campaignId, #trackingId, authentication.principal)")
    public ResponseEntity<?> getCampaignTracking(
            @PathVariable("campaignId") String campaignId,
            @PathVariable("trackingId") String trackingId,
            HttpServletRequest request) {
        Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository
                .findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
        if (!campaignTrackingOpt.isPresent()) {
            ApiResponse.sendError(404, "Id: " + trackingId + " not found!", request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Reponse successfully", campaignTrackingOpt.get(), request.getRequestURI());
    }

    @PostMapping(value = "/requirements")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isJoinedCampaignTracking(#campaignId, #trackingId, authentication.principal)")
    public ResponseEntity<?> addContentRequirement(
            @PathVariable("campaignId") String campaignId,
            @PathVariable("trackingId") String trackingId,
            @RequestBody PlatformRequirementDetailsTrackingRequest platformRequirementDetailsTrackingRequest,
            HttpServletRequest request) {
        try {
            Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository
                    .findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
            if (campaignTrackingOpt.isEmpty()) {
                return ApiResponse.sendError(404, "Tracking ID: " + trackingId + " not found", request.getRequestURI());
            }
            CampaignTracking tracking = campaignTrackingOpt.get();
            List<PlatformRequirementTracking> platformRequirementTrackings = tracking.getPlatformRequirementTracking();
            for (int index = 0; index < platformRequirementTrackings.size(); index++) {
                if (platformRequirementTrackings.get(index).getPlatform().toUpperCase()
                        .equals(platformRequirementDetailsTrackingRequest.getPlatform().toUpperCase())) {
                    platformRequirementTrackings.get(index).getDetails()
                            .get(platformRequirementDetailsTrackingRequest.getIndex())
                            .setRequirementDetailsTracking(
                                    platformRequirementDetailsTrackingRequest.getPostUrl());
                    tracking.setPlatformRequirementTracking(platformRequirementTrackings);
                    campaignTrackingRepository.save(tracking);
                    break;
                }
            }
            return ApiResponse.sendSuccess(200, "Response successfully",
                    null,
                    request.getRequestURI());

        } catch (Exception e) {
            return ApiResponse.sendError(500, "Internal server error: " + e.getMessage(), request.getRequestURI());
        }
    }

    // @PutMapping("requirements/{type}/{index}/status")
    // @PreAuthorize("hasRole('ROLE_BRAND') &&
    // @securityService.isJoinedCampaignTracking(#campaignId, #trackingId,
    // authentication.principal.userId) &&
    // @securityService.checkCampaignStatus(#campaignId,
    // 'PARTICIPATING',authentication.principal)")
    // public ResponseEntity<?> updateRequirementStatus(
    // @PathVariable("campaignId") String campaignId,
    // @PathVariable("trackingId") String trackingId,
    // @PathVariable("type") String type,
    // @PathVariable("index") int index,
    // @RequestBody StatusRequest statusRequest,
    // HttpServletRequest request) {
    // Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository
    // .findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
    // CampaignTracking tracking = campaignTrackingOpt.get();
    // Map<String, List<CampaignRequirement>> trackingMap =
    // tracking.getCampaignRequirementTracking();
    // List<CampaignRequirement> contents = trackingMap.get(type);
    // if (contents == null || index < 0 || index >= contents.size()) {
    // return ApiResponse.sendError(400, "Invalid path variable",
    // request.getRequestURI());
    // }
    // CampaignTracking updatedTracking = null;
    // try {
    // Status newStatus = Status.valueOf(statusRequest.getStatus());
    // CampaignRequirement content = contents.get(index);
    // content.setStatus(newStatus.toString());
    // tracking.setProcess(calculateProcess(trackingMap));
    // updatedTracking = campaignTrackingRepository.save(tracking);
    // } catch (Exception e) {
    // ApiResponse.sendError(404, e.getMessage(), request.getRequestURI());
    // }
    // return ApiResponse.sendSuccess(200, "Update status of campaign requirement
    // successfully", updatedTracking,
    // request.getRequestURI());
    // }
    @PutMapping("/requirements/confirm")
    @PreAuthorize("hasRole('ROLE_BRAND') && @securityService.isJoinedCampaignTracking(#campaignId, #trackingId, authentication.principal) && @securityService.checkCampaignStatus(#campaignId, 'PARTICIPATING',authentication.principal)")
    public ResponseEntity<?> updateRequirementStatus(
            @PathVariable("campaignId") String campaignId,
            @PathVariable("trackingId") String trackingId,
            @RequestBody PlatformRequirementDetailsTrackingRequest platformRequirementDetailsTrackingRequest,
            HttpServletRequest request) {
        Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository
                .findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
        if (!campaignTrackingOpt.isPresent()) {
            return ApiResponse.sendError(404, "Id: " + trackingId + " not found!", request.getRequestURI());
        }
        CampaignTracking tracking = campaignTrackingOpt.get();
        List<PlatformRequirementTracking> platformRequirementTrackings = tracking.getPlatformRequirementTracking();
        for (int index = 0; index < platformRequirementTrackings.size(); index++) {
            if (platformRequirementTrackings.get(index).getPlatform().toUpperCase()
                    .equals(platformRequirementDetailsTrackingRequest.getPlatform().toUpperCase())) {
                platformRequirementTrackings.get(index).getDetails()
                        .get(platformRequirementDetailsTrackingRequest.getIndex())
                        .setRequirementDetailsTracking(
                                platformRequirementDetailsTrackingRequest.getPostUrl());
                tracking.setStatus(platformRequirementDetailsTrackingRequest.getStatus());
                campaignTrackingRepository.save(tracking);
                break;
            }
        }
        double process = calculateProcess(tracking.getPlatformRequirementTracking());
        tracking.setProcess(process);
        if (process >= 100.0 || process >= 100) {
            tracking.setStatus("COMPLETED");
        }
        CampaignTracking updatedTracking = campaignTrackingRepository.save(tracking);
        return ApiResponse.sendSuccess(200, "Update status of campaign requirement successfully", updatedTracking,
                request.getRequestURI());
    }

    private double calculateProcess(List<PlatformRequirementTracking> platformRequirementTracking) {
        int totalContents = platformRequirementTracking.stream()
                .mapToInt(p -> p.getDetails() != null ? p.getDetails().size() : 0)
                .sum();
        int acceptedContents = platformRequirementTracking.stream()
                .flatMap(p -> p.getDetails() != null ? p.getDetails().stream() : java.util.stream.Stream.empty())
                .mapToInt(content -> {
                    String status = content.getStatus();
                    if (status == null) {
                        return 0;
                    }
                    return status.toUpperCase().equals("ACCEPTED") ? 1 : 0;
                })
                .sum();
        return totalContents > 0 ? (acceptedContents * 100.0 / totalContents) : 0.0;
    }

}
