package com.api.service;

import com.api.dto.ApiResponse;
import com.api.model.Application;
import com.api.model.Campaign;
import com.api.model.CampaignTracking;
<<<<<<< Updated upstream
=======
import com.api.model.ChatRoom;
import com.api.model.Status;
>>>>>>> Stashed changes
import com.api.repository.ApplicationRepository;
import com.api.repository.BrandRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
<<<<<<< Updated upstream
=======
import com.api.repository.CategoryRepository;
import com.api.repository.ChatRoomRepository;
>>>>>>> Stashed changes
import com.api.repository.InfluencerRepository;
import com.api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
<<<<<<< Updated upstream
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
=======
import java.util.Set;
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
=======
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
>>>>>>> Stashed changes

    public ResponseEntity<?> apply_Application(String influencerId, String campaignId, HttpServletRequest request) {
        Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + campaignId + " not found", request.getRequestURI());
        }
        Campaign campaign = campaignOpt.get();
        if (campaign.getInfluencerCountCurrent() >= campaign.getInfluencerCountExpected()) {
            return ApiResponse.sendError(400, "Campaign has enough participants", request.getRequestURI());
        }
        if (applicationRepository.existsByInfluencerIdAndCampaignId(influencerId, campaignId)) {
            return ApiResponse.sendError(400, "Already apply", request.getRequestURI());
        }
<<<<<<< Updated upstream
        Application application = applicationRepository.save(new Application(campaignId));
=======
        Application application = applicationRepository
                .save(new Application(campaignId, influencerId, campaign.getBrandId()));
        campaign.setApplicationTotal(campaign.getApplicationTotal() + 1);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
        List<CampaignTracking> campaignTrackings = campaignTrackingRepository.findAllByCampaignId(application.getCampaignId());
        if (campaignTrackings.size() >= campaignRepository.findById(application.getCampaignId()).get().getInfluencerCount()) {
=======
        Campaign campaign = campaignRepository.findById(application.getCampaignId()).get();
        if (campaign.getInfluencerCountCurrent() >= campaign.getInfluencerCountExpected()) {
>>>>>>> Stashed changes
            return ApiResponse.sendError(400, "Campaign has enough participants", request.getRequestURI());
        }
        if (application.getLimited() <= 0) {
            return ApiResponse.sendError(403, "Access is denied.",
                    request.getRequestURI());
        }
        campaign.setApplicationTotal(campaign.getApplicationTotal() + 1);
        campaignRepository.save(campaign);
        application.setLimited(application.getLimited() - 1);
        applicationRepository.save(application);
        return ApiResponse.sendSuccess(201, "Re-send apply for application successfully", application,
                request.getRequestURI());
    }

<<<<<<< Updated upstream
    public ResponseEntity<?> confirm_Application(String brandId, String applicationId, boolean accepted, HttpServletRequest request) {
        Optional<Application> applicationOpt = applicationRepository.findById(applicationId);
=======
    public ResponseEntity<?> getAllApplicationByBrand(int pageNumber, int pageSize, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Campaign> campaignPage = campaignRepository.findAllByBrandId(brandId, pageable);

        List<Campaign> campaigns = campaignPage.getContent();

        if (campaigns.isEmpty()) {
            ApiResponse.sendError(400, "Not found any campaign!", request.getRequestURI());
        }
        List<String> campaignIds = campaigns.stream()
                .map(Campaign::getCampaignId)
                .toList();
        List<Application> applications = applicationRepository.findAllByCampaignIdIn(campaignIds);
        Map<String, List<Application>> applicationsByCampaign = applications.stream()
                .collect(Collectors.groupingBy(Application::getCampaignId));

        List<ApplicationsByCampaignResponse> applicationsByCampaignResponses = campaigns.stream()
                .map(campaignResponse -> new ApplicationsByCampaignResponse(
                campaignResponse,
                applicationsByCampaign.getOrDefault(campaignResponse.getCampaignId(), Collections.emptyList()),
                categoryRepository))
                .toList();
        return ApiResponse.sendSuccess(200, "Reponse successfully", applicationsByCampaignResponses,
                request.getRequestURI());
    }

    public ResponseEntity<?> getAllApplicationByInfluencer(CustomUserDetails userDetails,
            HttpServletRequest request) {
        String influencerId = userDetails.getUserId();
        List<Application> applications = applicationRepository.findAllByInfluencerId(influencerId);
        return ApiResponse.sendSuccess(200, "Reponse successfully", applications, request.getRequestURI());
    }
    
//    public ResponseEntity<?> getAllApplicationByInfluencer(CustomUserDetails userDetails,
//            HttpServletRequest request) {
//        String influencerId = userDetails.getUserId();
//        List<Application> applications = applicationRepository.findAllByInfluencerId(influencerId);
//
//        if (applications.isEmpty()) {
//            return ApiResponse.sendError(400, "Not found any application for this influencer!", request.getRequestURI());
//        }
//
//        Set<String> campaignIds = applications.stream()
//                .map(Application::getCampaignId)
//                .collect(Collectors.toSet());
//
//        List<Campaign> campaigns = campaignRepository.findAllByCampaignIdIn(campaignIds);
//
//        Map<String, Campaign> campaignMap = campaigns.stream()
//                .collect(Collectors.toMap(Campaign::getCampaignId, campaign -> campaign));
//
//        Map<String, List<Application>> applicationsByCampaign = applications.stream()
//                .collect(Collectors.groupingBy(Application::getCampaignId));
//
//        List<ApplicationsByCampaignResponse> applicationsByCampaignResponses = campaignIds.stream()
//                .map(campaignId -> {
//                    Campaign campaign = campaignMap.get(campaignId);
//                    List<Application> appsForCampaign = applicationsByCampaign.getOrDefault(campaignId, Collections.emptyList());
//                    return new ApplicationsByCampaignResponse(
//                            campaign, 
//                            appsForCampaign,
//                            categoryRepository
//                    );
//                })
//                .collect(Collectors.toList());
//
//        return ApiResponse.sendSuccess(200, "Response successfully", applicationsByCampaignResponses,
//                request.getRequestURI());
//    }

    public ResponseEntity<?> confirm_Application(String applicationId, boolean accepted, CustomUserDetails userDetails,
            HttpServletRequest request) {
        String brandId = userDetails.getUserId();

        Optional<Application> applicationOpt = applicationRepository.findByApplicationIdAndBrandId(applicationId,
                brandId);
>>>>>>> Stashed changes
        if (!applicationOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + applicationId + " not found", request.getRequestURI());
        }
        Application application = applicationOpt.get();
<<<<<<< Updated upstream
        if (accepted) {
            application.setStatus("ACCEPTED");
            CampaignTracking campaignTracking = new CampaignTracking();
            campaignTracking.setCampaignTrackingId(applicationId);
            campaignTrackingRepository.save(campaignTracking);
=======
        Campaign campaign = campaignRepository.findById(application.getCampaignId()).get();
        List<Application> applications = applicationRepository.findAllByCampaignIdAndStatus(campaign.getCampaignId(),
                "ACCEPTED");
        if (applications.size() >= application.getLimited()) {
            return ApiResponse.sendError(400, "Reached to limitation", request.getRequestURI());
        }
        if (!application.getStatus().equals(Status.PENDING.toString())) {
            return ApiResponse.sendError(400, "Already " + application.getStatus().toLowerCase(), request.getRequestURI());
        }
        if (campaign.getInfluencerCountCurrent() >= campaign.getInfluencerCountExpected()) {
            return ApiResponse.sendError(400, "Reached to limitation", request.getRequestURI());
        }
        ChatRoom chatRoom = chatRoomRepository.findById(application.getCampaignId()).get();
        List<String> roomMate = chatRoom.getMembers();
        if (accepted) {
            application.setStatus("ACCEPTED");
            roomMate.add(application.getInfluencerId());
            chatRoom.setMembers(roomMate);
            chatRoomRepository.save(chatRoom);
            campaign.setInfluencerCountCurrent(campaign.getInfluencerCountCurrent() + 1);
            campaignRepository.save(campaign);
>>>>>>> Stashed changes
        } else {
            application.setStatus("REJECTED");
            roomMate.remove(application.getInfluencerId());
            chatRoom.setMembers(roomMate);
            chatRoomRepository.save(chatRoom);
        }
        return ApiResponse.sendSuccess(200, "Confirm apllication successfully", null, request.getRequestURI());
    }

}
