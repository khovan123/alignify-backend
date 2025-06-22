package com.api.service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.dto.ApiResponse;
import com.api.dto.response.ApplicationPlusInfluencer;
import com.api.dto.response.ApplicationsByBrandResponse;
import com.api.dto.response.ApplicationsByfInfluencerResponse;
import com.api.model.Application;
import com.api.model.Campaign;
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.model.Influencer;
import com.api.model.Status;
import com.api.model.User;
import com.api.repository.ApplicationRepository;
import com.api.repository.BrandRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

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
        @Autowired
        private CategoryRepository categoryRepository;
        @Autowired
        private ChatRoomRepository chatRoomRepository;
        @Autowired
        private ChatMessageRepository chatMessageRepository;

        public ResponseEntity<?> apply_Application(String campaignId, CustomUserDetails userDetails,
                        HttpServletRequest request) {
                String influencerId = userDetails.getUserId();
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
                Application application = applicationRepository
                                .save(new Application(campaignId, influencerId, campaign.getBrandId()));
                campaign.setApplicationTotal(campaign.getApplicationTotal() + 1);
                return ApiResponse.sendSuccess(201, "Send apply for application successfully", application,
                                request.getRequestURI());
        }

        public ResponseEntity<?> cancel_Application(String applicationId, CustomUserDetails userDetails,
                        HttpServletRequest request) {
                String influencerId = userDetails.getUserId();

                Optional<Application> applicationOpt = applicationRepository.findByApplicationIdAndInfluencerId(
                                applicationId,
                                influencerId);
                if (!applicationOpt.isPresent()) {
                        return ApiResponse.sendError(404, "id: " + applicationId + " not found",
                                        request.getRequestURI());
                }
                applicationRepository.delete(applicationOpt.get());
                return ApiResponse.sendSuccess(204, "Delete application successfully", null, request.getRequestURI());
        }

        public ResponseEntity<?> reApply_Application(String applicationId, CustomUserDetails userDetails,
                        HttpServletRequest request) {
                String influencerId = userDetails.getUserId();

                Optional<Application> applicationOpt = applicationRepository.findByApplicationIdAndInfluencerId(
                                applicationId,
                                influencerId);
                if (!applicationOpt.isPresent()) {
                        return ApiResponse.sendError(404, "id: " + applicationId + " not found",
                                        request.getRequestURI());
                }
                Application application = applicationOpt.get();
                Campaign campaign = campaignRepository.findById(application.getCampaignId()).get();
                if (campaign.getInfluencerCountCurrent() >= campaign.getInfluencerCountExpected()) {
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

        public ResponseEntity<?> getAllApplicationByBrand(int pageNumber, int pageSize, CustomUserDetails userDetails,
                        HttpServletRequest request) {
                String brandId = userDetails.getUserId();
                Pageable pageable = PageRequest.of(pageNumber, pageSize);
                Page<Campaign> campaignPage = campaignRepository.findAllByBrandIdAndStatusOrderByCreatedAtDesc(brandId,
                                "RECRUITING",
                                pageable);
                Optional<User> brandUser = userRepository.findById(brandId);
                List<Campaign> campaigns = campaignPage.getContent();

                if (campaigns.isEmpty()) {
                        return ApiResponse.sendSuccess(200, "You dont have any applications yet!", null,
                                        request.getRequestURI());
                }
                List<String> campaignIds = campaigns.stream()
                                .map(Campaign::getCampaignId)
                                .toList();
                List<Application> applications = applicationRepository.findAllByCampaignIdIn(campaignIds);

                List<ApplicationsByBrandResponse> applicationsByCampaignResponses = campaigns.stream()
                                .map(campaign -> {
                                        List<Application> appsForCampaign = applications.stream()
                                                        .filter(app -> app.getCampaignId()
                                                                        .equals(campaign.getCampaignId()))
                                                        .collect(Collectors.toList());

                                        if (campaign.getApplicationTotal() != appsForCampaign.size()) {
                                                campaign.setApplicationTotal(appsForCampaign.size());

                                                long acceptedCount = appsForCampaign.stream()
                                                                .filter(app -> Status.ACCEPTED.toString()
                                                                                .equals(app.getStatus()))
                                                                .count();

                                                if (campaign.getInfluencerCountCurrent() != acceptedCount) {
                                                        campaign.setInfluencerCountCurrent((int) acceptedCount);
                                                }
                                                campaignRepository.save(campaign);
                                        }

                                        List<ApplicationPlusInfluencer> appResponses = appsForCampaign.stream()
                                                        .map(app -> {
                                                                User user = userRepository
                                                                                .findById(app.getInfluencerId())
                                                                                .orElse(null);
                                                                Influencer influencer = influencerRepository
                                                                                .findById(app.getInfluencerId())
                                                                                .orElse(null);
                                                                return new ApplicationPlusInfluencer(user, influencer,
                                                                                app);
                                                        })
                                                        .collect(Collectors.toList());

                                        return new ApplicationsByBrandResponse(brandUser.get(), campaign, appResponses,
                                                        categoryRepository);
                                })
                                .collect(Collectors.toList());

                return ApiResponse.sendSuccess(200, "Reponse successfully", applicationsByCampaignResponses,
                                request.getRequestURI());
        }

        public ResponseEntity<?> getAllApplicationByInfluencer(CustomUserDetails userDetails,
                        HttpServletRequest request) {
                String influencerId = userDetails.getUserId();
                List<Application> applications = applicationRepository.findAllByInfluencerId(influencerId);
                Optional<User> user = userRepository.findById(influencerId);
                if (applications.isEmpty()) {
                        return ApiResponse.sendSuccess(200, "You dont have any applications yet!", null,
                                        request.getRequestURI());
                }

                Set<String> campaignIds = applications.stream()
                                .map(Application::getCampaignId)
                                .collect(Collectors.toSet());

                List<Campaign> campaigns = campaignRepository.findAllByCampaignIdIn(campaignIds);

                Map<String, Campaign> campaignMap = campaigns.stream()
                                .collect(Collectors.toMap(Campaign::getCampaignId, campaign -> campaign));

                Map<String, List<Application>> applicationsByCampaign = applications.stream()
                                .collect(Collectors.groupingBy(Application::getCampaignId));

                List<ApplicationsByfInfluencerResponse> applicationsByCampaignResponses = campaignIds.stream()
                                .map(campaignId -> {
                                        Campaign campaign = campaignMap.get(campaignId);
                                        List<Application> appsForCampaign = applicationsByCampaign.getOrDefault(
                                                        campaignId,
                                                        Collections.emptyList());
                                        if (campaign.getApplicationTotal() != appsForCampaign.size()) {
                                                campaign.setApplicationTotal(appsForCampaign.size());

                                                long acceptedCount = appsForCampaign.stream()
                                                                .filter(app -> Status.ACCEPTED.toString()
                                                                                .equals(app.getStatus()))
                                                                .count();

                                                if (campaign.getInfluencerCountCurrent() != acceptedCount) {
                                                        campaign.setInfluencerCountCurrent((int) acceptedCount);
                                                }

                                                campaignRepository.save(campaign);
                                        }
                                        return new ApplicationsByfInfluencerResponse(
                                                        user.get(),
                                                        campaign,
                                                        appsForCampaign,
                                                        categoryRepository);
                                })
                                .collect(Collectors.toList());

                return ApiResponse.sendSuccess(200, "Response successfully", applicationsByCampaignResponses,
                                request.getRequestURI());
        }

        public ResponseEntity<?> confirm_Application(String applicationId, boolean accepted,
                        CustomUserDetails userDetails,
                        HttpServletRequest request) {
                String brandId = userDetails.getUserId();

                Optional<Application> applicationOpt = applicationRepository.findByApplicationIdAndBrandId(
                                applicationId,
                                brandId);
                if (!applicationOpt.isPresent()) {
                        return ApiResponse.sendError(404, "Applicationt with id: " + applicationId + " not found",
                                        request.getRequestURI());
                }
                Application application = applicationOpt.get();
                Optional<Campaign> campaignOpt = campaignRepository.findById(application.getCampaignId());
                if (!campaignOpt.isPresent()) {
                        return ApiResponse.sendError(404, "Campaign not found", request.getRequestURI());
                }
                Campaign campaign = campaignOpt.get();
                List<Application> applications = applicationRepository.findAllByCampaignIdAndStatus(
                                campaign.getCampaignId(),
                                "ACCEPTED");
                if (applications.size() >= application.getLimited()) {
                        return ApiResponse.sendError(400, "Reached to limitation", request.getRequestURI());
                }
                if (!application.getStatus().equals(Status.PENDING.toString())) {
                        return ApiResponse.sendError(400, "Already " + application.getStatus().toLowerCase(),
                                        request.getRequestURI());
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
                        ChatMessage chatMessage = new ChatMessage();
                        User user = userRepository.findById(brandId).get();
                        chatMessage.setMessage("Xin chào " + user.getName() + " !");
                        chatMessage.setName(user.getName());
                        chatMessage.setSendAt(LocalDateTime.now());
                        chatMessage.setChatRoomId(chatRoom.getChatRoomId());
                        chatMessage.setUserId("#SYS");
                        chatMessageRepository.save(chatMessage);
                } else {
                        application.setStatus("REJECTED");
                        roomMate.remove(application.getInfluencerId());
                        chatRoom.setMembers(roomMate);
                        chatRoomRepository.save(chatRoom);
                }
                applicationRepository.save(application);
                User user = userRepository.findById(application.getInfluencerId()).orElse(null);
                Influencer influencer = influencerRepository.findById(application.getInfluencerId()).orElse(null);
                ApplicationPlusInfluencer applicationPlusInfluencer = new ApplicationPlusInfluencer(user, influencer,
                                application);
                return ApiResponse.sendSuccess(200, "Confirm apllication successfully", applicationPlusInfluencer,
                                request.getRequestURI());
        }

}