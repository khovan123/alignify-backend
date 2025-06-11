package com.api.service;

import com.api.dto.ApiResponse;
import com.api.model.Application;
import com.api.model.Campaign;
import com.api.model.CampaignTracking;
import com.api.repository.ApplicationRepository;
import com.api.repository.BrandRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private InfluencerRepository influencerRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignTrackingRepository campaignTrackingRepository;

    public ResponseEntity<?> apply_Application(String campaignId, CustomUserDetails userDetails, HttpServletRequest request) {
        String influencerId = userDetails.getUserId();
        Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + campaignId + " not found", request.getRequestURI());
        }
        List<CampaignTracking> campaignTrackings = campaignTrackingRepository.findAllByCampaignId(campaignId);
        if (campaignTrackings.size() >= campaignOpt.get().getInfluencerCount()) {
            return ApiResponse.sendError(400, "Campaign has enough participants", request.getRequestURI());
        }
        if (applicationRepository.existsByInfluencerIdAndCampaignId(influencerId, campaignId)) {
            return ApiResponse.sendError(400, "Already apply", request.getRequestURI());
        }
        Application application = applicationRepository.save(new Application(campaignId, campaignId, influencerId, campaignId));
        return ApiResponse.sendSuccess(201, "Send apply for application successfully", application,
                request.getRequestURI());
    }

    public ResponseEntity<?> cancel_Application(String applicationId, CustomUserDetails userDetails, HttpServletRequest request) {
        String influencerId = userDetails.getUserId();

        Optional<Application> applicationOpt = applicationRepository.findByApplicationIdAndInfluencerId(applicationId, influencerId);
        if (!applicationOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + applicationId + " not found", request.getRequestURI());
        }
        applicationRepository.delete(applicationOpt.get());
        return ApiResponse.sendSuccess(204, "Delete application successfully", null, request.getRequestURI());
    }

    public ResponseEntity<?> reApply_Application(String applicationId, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String influencerId = userDetails.getUserId();

        Optional<Application> applicationOpt = applicationRepository.findByApplicationIdAndInfluencerId(applicationId, influencerId);
        if (!applicationOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + applicationId + " not found", request.getRequestURI());
        }
        Application application = applicationOpt.get();
        List<CampaignTracking> campaignTrackings = campaignTrackingRepository.findAllByCampaignId(application.getCampaignId());
        if (campaignTrackings.size() >= campaignRepository.findById(application.getCampaignId()).get().getInfluencerCount()) {
            return ApiResponse.sendError(400, "Campaign has enough participants", request.getRequestURI());
        }
        if (application.getLimited() <= 0) {
            return ApiResponse.sendError(403, "Access is denied. You have reached your limit.",
                    request.getRequestURI());
        }
        application.setLimited(application.getLimited() - 1);
        applicationRepository.save(application);
        return ApiResponse.sendSuccess(201, "Re-send apply for application successfully", application,
                request.getRequestURI());
    }

    public ResponseEntity<?> confirm_Application(String applicationId, boolean accepted, CustomUserDetails userDetails, HttpServletRequest request) {
        String brandId = userDetails.getUserId();

        Optional<Application> applicationOpt = applicationRepository.findByApplicationIdAndBrandId(applicationId, brandId);
        if (!applicationOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + applicationId + " not found", request.getRequestURI());
        }
        Application application = applicationOpt.get();
        Campaign campaign = campaignRepository.findById(application.getCampaignId()).get();
        if (accepted) {
            application.setStatus("ACCEPTED");
            CampaignTracking campaignTracking = new CampaignTracking(application.getCampaignId(), brandId, application.getInfluencerId(), campaign.getCampaignRequirements());
            campaignTracking.setCampaignTrackingId(applicationId);
            campaignTrackingRepository.save(campaignTracking);
        } else {
            application.setStatus("REJECTED");
        }
        return ApiResponse.sendSuccess(200, "Confirm apllication successfully", null, request.getRequestURI());
    }

}
