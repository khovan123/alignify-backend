package com.api.controller.campaign;

import java.util.List;
import java.util.Map;
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
import com.api.dto.request.CampaignRequirementRequest;
import com.api.dto.request.StatusRequest;
import com.api.model.CampaignRequirement;
import com.api.model.CampaignTracking;
import com.api.model.Status;
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

    @PostMapping("/requirements")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') && @securityService.isJoinedCampaignTracking(#campaignId, #trackingId, authentication.principal) && @securityService.isOnGoingCampaign(#campaignId, authentication.principal)")
    public ResponseEntity<?> addContentRequirement(
            @PathVariable("campaignId") String campaignId,
            @PathVariable("trackingId") String trackingId,
            @RequestBody Map<String, List<CampaignRequirementRequest>> requirementsMap,
            HttpServletRequest request) {
        Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository
                .findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
        if (!campaignTrackingOpt.isPresent()) {
            return ApiResponse.sendError(404, "Id: " + trackingId + " not found!", request.getRequestURI());
        }
        CampaignTracking tracking = campaignTrackingOpt.get();
        Map<String, List<CampaignRequirement>> trackingMap = tracking.getCampaignRequirementTracking();
        for (Map.Entry<String, List<CampaignRequirementRequest>> entry : requirementsMap.entrySet()) {
            String type = entry.getKey();
            List<CampaignRequirementRequest> contentRequests = entry.getValue();
            List<CampaignRequirement> requirements = trackingMap.get(type);
            if (requirements == null) {
                return ApiResponse.sendError(400, "No requirements found for type: " + type, request.getRequestURI());
            }
            for (CampaignRequirementRequest contentRequest : contentRequests) {
                CampaignRequirement content = new CampaignRequirement(
                        contentRequest.getIndex(),
                        contentRequest.getImageUrl(),
                        contentRequest.getPostUrl());
                if (content.getIndex() < 0 || content.getIndex() >= requirements.size()) {
                    return ApiResponse.sendError(400, "Invalid index for type: " + type, request.getRequestURI());
                }
                requirements.remove(content.getIndex());
                requirements.add(content.getIndex(), content);
            }
        }
        CampaignTracking updatedTracking = campaignTrackingRepository.save(tracking);
        return ApiResponse.sendSuccess(200, "Response successfully", updatedTracking.getCampaignRequirementTracking(),
                request.getRequestURI());
    }

    @PutMapping("requirements/{type}/{index}/status")
    @PreAuthorize("hasRole('ROLE_BRAND') && @securityService.isJoinedCampaignTracking(#campaignId, #trackingId, authentication.principal.userId) && @securityService.isOnGoingCampaign(#campaignId, authentication.principal)")
    public ResponseEntity<?> updateRequirementStatus(
            @PathVariable("campaignId") String campaignId,
            @PathVariable("trackingId") String trackingId,
            @PathVariable("type") String type,
            @PathVariable("index") int index,
            @RequestBody StatusRequest statusRequest,
            HttpServletRequest request) {
        Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository
                .findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
        CampaignTracking tracking = campaignTrackingOpt.get();
        Map<String, List<CampaignRequirement>> trackingMap = tracking.getCampaignRequirementTracking();
        List<CampaignRequirement> contents = trackingMap.get(type);
        if (contents == null || index < 0 || index >= contents.size()) {
            return ApiResponse.sendError(400, "Invalid path variable", request.getRequestURI());
        }
        CampaignTracking updatedTracking = null;
        try {
            Status newStatus = Status.valueOf(statusRequest.getStatus());
            CampaignRequirement content = contents.get(index);
            content.setStatus(newStatus.toString());

            tracking.setProcess(calculateProcess(trackingMap));

            updatedTracking = campaignTrackingRepository.save(tracking);
        } catch (Exception e) {
            ApiResponse.sendError(404, e.getMessage(), request.getRequestURI());
        }
        return ApiResponse.sendSuccess(200, "Update status of campaign requirement successfully", updatedTracking,
                request.getRequestURI());
    }

    @PutMapping("/requirements/confirm")
    @PreAuthorize("hasRole('ROLE_BRAND') && @securityService.isJoinedCampaignTracking(#campaignId, #trackingId, authentication.principal) && @securityService.isOnGoingCampaign(#campaignId, authentication.principal)")
    public ResponseEntity<?> updateRequirementStatus(
            @PathVariable("campaignId") String campaignId,
            @PathVariable("trackingId") String trackingId,
            @RequestBody Map<String, List<CampaignRequirementRequest>> requirementsMap,
            HttpServletRequest request) {
        Optional<CampaignTracking> campaignTrackingOpt = campaignTrackingRepository
                .findByCampaignTrackingIdAndCampaignId(trackingId, campaignId);
        if (!campaignTrackingOpt.isPresent()) {
            return ApiResponse.sendError(404, "Id: " + trackingId + " not found!", request.getRequestURI());
        }
        CampaignTracking tracking = campaignTrackingOpt.get();
        Map<String, List<CampaignRequirement>> trackingMap = tracking.getCampaignRequirementTracking();
        for (Map.Entry<String, List<CampaignRequirementRequest>> entry : requirementsMap.entrySet()) {
            String type = entry.getKey();
            List<CampaignRequirementRequest> contentRequests = entry.getValue();
            List<CampaignRequirement> requirements = trackingMap.get(type);
            if (requirements == null) {
                return ApiResponse.sendError(400, "No requirements found for type: " + type, request.getRequestURI());
            }
            for (CampaignRequirementRequest contentRequest : contentRequests) {
                int idx = contentRequest.getIndex();
                if (idx < 0 || idx >= requirements.size()) {
                    return ApiResponse.sendError(400, "Invalid index for type: " + type, request.getRequestURI());
                }
                CampaignRequirement content = requirements.get(idx);
                content.setStatus(contentRequest.getStatus());
            }
        }
        tracking.setProcess(calculateProcess(trackingMap));
        CampaignTracking updatedTracking = campaignTrackingRepository.save(tracking);
        return ApiResponse.sendSuccess(200, "Update status of campaign requirement successfully", updatedTracking,
                request.getRequestURI());
    }

    private double calculateProcess(Map<String, List<CampaignRequirement>> trackingMap) {
        int totalContents = trackingMap.values().stream().mapToInt(List::size).sum();
        int acceptedContents = trackingMap.values().stream()
                .flatMap(List::stream)
                .mapToInt(content -> {
                    String status = content.getStatus();
                    if (status == null)
                        return 0;
                    return status.toUpperCase().equals("ACCEPTED") ? 1 : 0;
                })
                .sum();
        return totalContents > 0 ? (acceptedContents * 100.0 / totalContents) : 0.0;
    }

}
