package com.api.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.api.dto.ApiResponse;
import com.api.dto.response.ApplicationPlusInfluencer;
import com.api.dto.response.ApplicationsByBrandResponse;
import com.api.dto.response.ApplicationsByfInfluencerResponse;
import com.api.model.Application;
import com.api.model.Campaign;
import com.api.model.CampaignTracking;
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.model.Influencer;
import com.api.model.Status;
import com.api.model.User;
import com.api.repository.ApplicationRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ApplicationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InfluencerRepository influencerRepository;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private CampaignTrackingRepository campaignTrackingRepository;
    @Autowired
    private FileStorageService fileStorageService;

    public ResponseEntity<?> apply_Application(
            String campaignId,
            MultipartFile cv,
            CustomUserDetails userDetails,
            HttpServletRequest request) {
        String influencerId = userDetails.getUserId();
        Optional<Campaign> campaignOpt = campaignRepository.findById(campaignId);
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + campaignId + " not found", request.getRequestURI());
        }
        Campaign campaign = campaignOpt.get();
        if (campaign.getJoinedInfluencerIds().size() >= campaign.getInfluencerCountExpected()) {
            return ApiResponse.sendError(400, "Campaign has enough participants", request.getRequestURI());
        }
        if (campaign.getAppliedInfluencerIds().contains(influencerId)
                || applicationRepository.existsByInfluencerIdAndCampaignId(influencerId, campaignId)) {
            return ApiResponse.sendError(400, "Already apply", request.getRequestURI());
        }
        String fileUrl = null;
        try {
            fileUrl = fileStorageService.storeFile(cv);
        } catch (Exception e) {
            return ApiResponse.sendError(500, e.getMessage(), request.getRequestURI());
        }
        Application application = applicationRepository
                .save(new Application(campaignId, influencerId, campaign.getBrandId(), fileUrl));
        List<String> updatedAppliedInfluencerIds = campaign.getAppliedInfluencerIds() == null
                ? new java.util.ArrayList<>()
                : new java.util.ArrayList<>(campaign.getAppliedInfluencerIds());
        updatedAppliedInfluencerIds.add(influencerId);
        campaign.setAppliedInfluencerIds(updatedAppliedInfluencerIds);
        campaign.setApplicationTotal(campaign.getApplicationTotal() + 1);
        campaignRepository.save(campaign);
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
        Optional<Campaign> campaignOpt = campaignRepository.findById(applicationOpt.get().getCampaignId());
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "id: " + applicationOpt.get().getCampaignId() + " not found",
                    request.getRequestURI());
        }
        Campaign campaign = campaignOpt.get();
        applicationRepository.delete(applicationOpt.get());
        List<String> updatedAppliedInfluencerIds = campaign.getAppliedInfluencerIds() == null
                ? new java.util.ArrayList<>()
                : new java.util.ArrayList<>(campaign.getAppliedInfluencerIds());
        updatedAppliedInfluencerIds.remove(influencerId);
        campaign.setAppliedInfluencerIds(updatedAppliedInfluencerIds);
        campaignRepository.save(campaign);
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
        if (campaign.getJoinedInfluencerIds().size() >= campaign.getInfluencerCountExpected()) {
            return ApiResponse.sendError(400, "Campaign has enough participants", request.getRequestURI());
        }
        if (application.getLimited() <= 0) {
            return ApiResponse.sendError(403, "Access is denied.",
                    request.getRequestURI());
        }
        if (campaign.getAppliedInfluencerIds().contains(influencerId)
                || applicationRepository.existsByInfluencerIdAndCampaignId(influencerId,
                campaign.getCampaignId())) {
            return ApiResponse.sendError(400, "Already apply", request.getRequestURI());
        }
        List<String> updatedAppliedInfluencerIds = campaign.getAppliedInfluencerIds() == null
                ? new java.util.ArrayList<>()
                : new java.util.ArrayList<>(campaign.getAppliedInfluencerIds());
        updatedAppliedInfluencerIds.add(influencerId);
        campaign.setApplicationTotal(campaign.getApplicationTotal() + 1);
        campaign.setAppliedInfluencerIds(updatedAppliedInfluencerIds);
        campaignRepository.save(campaign);
        application.setLimited(application.getLimited() - 1);
        applicationRepository.save(application);
        return ApiResponse.sendSuccess(201, "Re-send apply for application successfully", application,
                request.getRequestURI());
    }

    public ResponseEntity<?> getAllApplicationByBrand(CustomUserDetails userDetails,
                                                      HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        Optional<User> brandUser = userRepository.findById(brandId);
        List<Campaign> campaigns = campaignRepository.findAllByBrandIdAndStatusOrderByCreatedAtDesc(brandId,
                "RECRUITING");
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

                        List<String> joinedInfluencerIds = appsForCampaign.stream()
                                .filter(app -> app.getStatus()
                                        .equals(Status.ACCEPTED.toString()))
                                .map(Application::getInfluencerId)
                                .toList();

                        campaign.setJoinedInfluencerIds(joinedInfluencerIds);
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

        Set<String> brandIds = campaigns.stream().map(Campaign::getBrandId).collect(Collectors.toSet());
        Map<String, User> brandUserMap = userRepository.findAllById(brandIds).stream()
                .collect(Collectors.toMap(User::getUserId, u -> u));

        Map<String, List<Application>> applicationsByCampaign = applications.stream()
                .collect(Collectors.groupingBy(Application::getCampaignId));

        List<ApplicationsByfInfluencerResponse> applicationsByCampaignResponses = campaignIds.stream()
                .map(campaignId -> {
                    Campaign campaign = campaignMap.get(campaignId);
                    if (campaign == null)
                        return null;
                    User brandUser = brandUserMap.get(campaign.getBrandId());
                    List<Application> appsForCampaign = applicationsByCampaign
                            .getOrDefault(campaignId, Collections.emptyList());
                    int appSize = appsForCampaign.size();
                    boolean changed = false;
                    if (campaign.getApplicationTotal() != appSize) {
                        campaign.setApplicationTotal(appSize);
                        changed = true;
                    }
                    List<String> joinedInfluencerIds = appsForCampaign.stream()
                            .filter(app -> app.getStatus()
                                    .equals(Status.ACCEPTED.toString()))
                            .map(Application::getInfluencerId)
                            .toList();

                    if (campaign.getJoinedInfluencerIds().size() != joinedInfluencerIds.size()) {
                        campaign.setJoinedInfluencerIds(joinedInfluencerIds);
                        changed = true;
                    }
                    if (changed) {
                        campaignRepository.save(campaign);
                    }
                    return new ApplicationsByfInfluencerResponse(brandUser, campaign,
                            appsForCampaign, categoryRepository);
                })
                .filter(r -> r != null)
                .collect(Collectors.toList());

        return ApiResponse.sendSuccess(200, "Response successfully", applicationsByCampaignResponses,
                request.getRequestURI());
    }

    // public ResponseEntity<?> confirm_Application(String applicationId, boolean
    // accepted,
    // CustomUserDetails userDetails,
    // HttpServletRequest request) {
    // String brandId = userDetails.getUserId();

    // Optional<Application> applicationOpt =
    // applicationRepository.findByApplicationIdAndBrandId(
    // applicationId,
    // brandId);
    // if (!applicationOpt.isPresent()) {
    // return ApiResponse.sendError(404, "Applicationt with id: " + applicationId +
    // " not found",
    // request.getRequestURI());
    // }
    // Application application = applicationOpt.get();
    // Optional<Campaign> campaignOpt =
    // campaignRepository.findById(application.getCampaignId());
    // if (!campaignOpt.isPresent()) {
    // return ApiResponse.sendError(404, "Campaign not found",
    // request.getRequestURI());
    // }
    // Campaign campaign = campaignOpt.get();
    // List<Application> applications =
    // applicationRepository.findAllByCampaignIdAndStatus(
    // campaign.getCampaignId(),
    // "ACCEPTED");
    // if (applications.size() >= application.getLimited()) {
    // return ApiResponse.sendError(400, "Reached to limitation",
    // request.getRequestURI());
    // }
    // if (!application.getStatus().equals(Status.PENDING.toString())) {
    // return ApiResponse.sendError(400, "Already " +
    // application.getStatus().toLowerCase(),
    // request.getRequestURI());
    // }
    // if (campaign.getInfluencerCountCurrent() >=
    // campaign.getInfluencerCountExpected()) {
    // return ApiResponse.sendError(400, "Reached to limitation",
    // request.getRequestURI());
    // }
    // ChatRoom chatRoom =
    // chatRoomRepository.findById(application.getCampaignId()).get();
    // List<String> roomMate = chatRoom.getMembers();
    // if (accepted) {
    // application.setStatus("ACCEPTED");
    // roomMate.add(application.getInfluencerId());
    // chatRoom.setMembers(roomMate);
    // chatRoomRepository.save(chatRoom);
    // campaign.setInfluencerCountCurrent(campaign.getInfluencerCountCurrent() + 1);
    // campaignRepository.save(campaign);
    // ChatMessage chatMessage = new ChatMessage();
    // User user =
    // userRepository.findById(applicationOpt.get().getInfluencerId()).get();
    // chatMessage.setMessage(user.getName() + " đã vào phòng chat.");
    // chatMessage.setName(user.getName());
    // chatMessage.setSendAt(ZonedDateTime.now());
    // chatMessage.setChatRoomId(chatRoom.getChatRoomId());
    // chatMessage.setUserId("#SYS");
    // chatMessageRepository.save(chatMessage);
    // } else {
    // application.setStatus("REJECTED");
    // roomMate.remove(application.getInfluencerId());
    // chatRoom.setMembers(roomMate);
    // chatRoomRepository.save(chatRoom);
    // List<String> updatedAppliedInfluencerIds = campaign.getAppliedInfluencerIds()
    // == null
    // ? new java.util.ArrayList<>()
    // : new java.util.ArrayList<>(campaign.getAppliedInfluencerIds());
    // updatedAppliedInfluencerIds.remove(application.getInfluencerId());
    // campaign.setAppliedInfluencerIds(updatedAppliedInfluencerIds);
    // campaignRepository.save(campaign);
    // }
    // applicationRepository.save(application);
    // User user =
    // userRepository.findById(application.getInfluencerId()).orElse(null);
    // Influencer influencer =
    // influencerRepository.findById(application.getInfluencerId()).orElse(null);
    // ApplicationPlusInfluencer applicationPlusInfluencer = new
    // ApplicationPlusInfluencer(user, influencer,
    // application);
    // return ApiResponse.sendSuccess(200, "Confirm apllication successfully",
    // applicationPlusInfluencer,
    // request.getRequestURI());
    // }

    public ResponseEntity<?> confirm_Application(String applicationId, boolean accepted,
                                                 CustomUserDetails userDetails,
                                                 HttpServletRequest request) {
        String brandId = userDetails.getUserId();

        Optional<Application> applicationOpt = applicationRepository.findByApplicationIdAndBrandId(
                applicationId,
                brandId);
        if (!applicationOpt.isPresent()) {
            return ApiResponse.sendError(404, "Application with id: " + applicationId + " not found",
                    request.getRequestURI());
        }
        Application application = applicationOpt.get();
        Optional<Campaign> campaignOpt = campaignRepository.findById(application.getCampaignId());
        if (!campaignOpt.isPresent()) {
            return ApiResponse.sendError(404, "Campaign not found", request.getRequestURI());
        }
        Campaign campaign = campaignOpt.get();
        if (campaign.getJoinedInfluencerIds().size() >= campaign.getInfluencerCountExpected()) {
            return ApiResponse.sendError(400, "Reached to limitation", request.getRequestURI());
        }
        if (!application.getStatus().equals(Status.PENDING.toString())) {
            return ApiResponse.sendError(400, "Already " + application.getStatus().toLowerCase(),
                    request.getRequestURI());
        }
        ChatRoom chatRoom = chatRoomRepository.findById(application.getCampaignId()).orElse(null);
        if (chatRoom == null) {
            chatRoom = new ChatRoom(campaign.getCampaignId(), brandId, campaign.getCampaignName(),
                    campaign.getImageUrl());
        }
        List<String> roomMate = chatRoom.getMembers() == null ? new java.util.ArrayList<>()
                : new java.util.ArrayList<>(chatRoom.getMembers());
        List<String> joinedInfluencerIds = campaign.getJoinedInfluencerIds();
        if (accepted) {
            application.setStatus("ACCEPTED");
            if (!roomMate.contains(application.getInfluencerId())) {
                roomMate.add(application.getInfluencerId());
            }
            CampaignTracking campaignTracking = new CampaignTracking(applicationId,
                    application.getCampaignId(),
                    brandId, application.getInfluencerId(), campaign.getCampaignRequirements());
            campaignTracking.setCampaignTrackingId(applicationId);
            campaignTrackingRepository.save(campaignTracking);
            chatRoom.setMembers(roomMate);
            chatRoom.setCreatedAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            chatRoomRepository.save(chatRoom);
            joinedInfluencerIds.add(application.getInfluencerId());
            campaign.setJoinedInfluencerIds(joinedInfluencerIds);
            campaignRepository.save(campaign);
            ChatMessage chatMessage = new ChatMessage();
            User user = userRepository.findById(applicationOpt.get().getInfluencerId()).orElse(null);
            if (user != null) {
                chatMessage.setMessage(user.getName() + " đã vào phòng chat.");
                chatMessage.setName(user.getName());
            } else {
                chatMessage.setMessage("Influencer đã vào phòng chat.");
                chatMessage.setName("Influencer");
            }
            chatMessage.setSendAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            chatMessage.setChatRoomId(chatRoom.getChatRoomId());
            chatMessage.setUserId("#SYS");
            chatMessageRepository.save(chatMessage);
        } else {
            application.setStatus("REJECTED");
            roomMate.remove(application.getInfluencerId());
            chatRoom.setMembers(roomMate);
            List<String> updatedAppliedInfluencerIds = campaign.getAppliedInfluencerIds() == null
                    ? new java.util.ArrayList<>()
                    : new java.util.ArrayList<>(campaign.getAppliedInfluencerIds());
            updatedAppliedInfluencerIds.remove(application.getInfluencerId());
            campaign.setAppliedInfluencerIds(updatedAppliedInfluencerIds);
            if (!campaign.getJoinedInfluencerIds().isEmpty()
                    && Status.ACCEPTED.toString().equals(application.getStatus())) {
                joinedInfluencerIds.remove(application.getInfluencerId());
                campaign.setJoinedInfluencerIds(joinedInfluencerIds);
            }
        }
        campaignRepository.save(campaign);
        chatRoomRepository.save(chatRoom);
        applicationRepository.save(application);
        User user = userRepository.findById(application.getInfluencerId()).orElse(null);
        Influencer influencer = influencerRepository.findById(application.getInfluencerId()).orElse(null);
        ApplicationPlusInfluencer applicationPlusInfluencer = new ApplicationPlusInfluencer(user, influencer,
                application);
        return ApiResponse.sendSuccess(200, "Confirm application successfully", applicationPlusInfluencer,
                request.getRequestURI());
    }

}