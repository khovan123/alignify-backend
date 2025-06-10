package com.api.service;

import com.api.dto.ApiResponse;
import com.api.model.Application;
import com.api.model.Campaign;
import com.api.repository.ApplicationRepository;
import com.api.repository.BrandRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    
    public ResponseEntity<?> apply_Application(String influencerId, String campaignId, HttpServletRequest request) {
        Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + campaignId + " not found", request.getRequestURI());
        }
        if (applicationRepository.existsByInfluencerIdAndCampaignId(influencerId, campaignId)) {
            return ApiResponse.sendError(400, "id: " + campaignId + " exists", request.getRequestURI());
        }
        Application application = applicationRepository.save(new Application(campaignId));
        return ApiResponse.sendSuccess(201, "Send apply for application successfully", application,
                request.getRequestURI());
    }
    
    public ResponseEntity<?> cancel_Application(String influencerId, String applicationId, HttpServletRequest request) {
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (!applicationOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + applicationId + " not found", request.getRequestURI());
        }
        applicationRepository.delete(applicationOpt.get());
        return ApiResponse.sendSuccess(204, "Delete application successfully", null, request.getRequestURI());
    }
    
    public ResponseEntity<?> reApply_Application(String influencerId, String applicationId,
            HttpServletRequest request) {
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (!applicationOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + applicationId + " not found", request.getRequestURI());
        }
        Application application = applicationOpt.get();
        if (application.getLimited() <= 0) {
            return ApiResponse.sendError(403, "Access is denied. You have reached your limit.",
                    request.getRequestURI());
        }
        application.setLimited(application.getLimited() - 1);
        applicationRepository.save(application);
        return ApiResponse.sendSuccess(201, "Re-send apply for application successfully", application,
                request.getRequestURI());
    }
    
    public ResponseEntity<?> confirm_Application(String brandId, String applicationId, boolean accepted, HttpServletRequest request) {
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
        if (!applicationOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + applicationId + " not found", request.getRequestURI());
        }
        Application application = applicationOpt.get();
        if (accepted) {
            application.setStatus("ACCEPTED");
        } else {
            application.setStatus("REJECTED");
        }
        return ApiResponse.sendSuccess(200, "Confirm apllication successfully", null, request.getRequestURI());
    }
    
    
    
}
