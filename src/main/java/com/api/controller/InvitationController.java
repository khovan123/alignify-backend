package com.api.controller;

import com.api.dto.ApiResponse;
import com.api.dto.request.InvitationRequest;
import com.api.model.Campaign;
import com.api.model.ChatRoom;
import com.api.model.Invitation;
import com.api.repository.ApplicationRepository;
import com.api.repository.BrandRepository;
import com.api.repository.CampaignRepository;
import com.api.repository.CampaignTrackingRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.ChatRoomRepository;
import com.api.repository.InfluencerRepository;
import com.api.repository.InvitationRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class InvitationController {

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
    private InvitationRepository invitationRepository;

    @PostMapping("/invitations/campaigns/{campaignId}")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isCampaignOwner(#campaignId, authentication.principal) and (@securityService.checkCampaignStatus(#campaignId,'PENDING',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'DRAFT',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
    public ResponseEntity<?> sendInvatation(@PathVariable("campaignId") String campaignId, @RequestBody InvitationRequest invitationRequest, CustomUserDetails userDetails, HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        Campaign campaign = campaignRepository.findByCampaignIdAndBrandId(campaignId, brandId).get();
        if (campaign.getInfluencerCountExpected() <= campaign.getInfluencerCountCurrent()) {
            return ApiResponse.sendError(400, "Your campaign is enough Influencer", request.getRequestURI());
        }
        if (invitationRepository.existsByCampaignIdAndInfluencerId(campaignId, invitationRequest.getInfluencerId())) {
            return ApiResponse.sendError(400, "Already sent invitation", request.getRequestURI());
        }
        ChatRoom chatRoom = chatRoomRepository.findById(campaignId).get();
        if (chatRoom.getMembers().contains(invitationRequest.getInfluencerId())) {
            return ApiResponse.sendError(400, "Already in this campaign", request.getRequestURI());
        }

        Invitation invitation = new Invitation(brandId, campaign.getCampaignId(), invitationRequest.getInfluencerId(), invitationRequest.getMessage());
        invitationRepository.save(invitation);

        return ApiResponse.sendSuccess(201, "Create invitation successfully", invitation, request.getRequestURI());
    }

    @PostMapping("/invitations/{invitationId}/confirm")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isJoinedInvitation(#invitationId, authentication.principal) and (@securityService.checkCampaignStatus(#campaignId,'PENDING',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'DRAFT',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
    public ResponseEntity<?> confirmInvatation(@RequestParam("accepted") boolean accepted, @PathVariable("invitationId") String invitationId, CustomUserDetails userDetails, HttpServletRequest request) {
        String influencerId = userDetails.getUserId();
        Invitation invitation = invitationRepository.findById(invitationId).get();
        Campaign campaign = campaignRepository.findByCampaignIdAndBrandId(invitation.getCampaignId(), invitation.getBrandId()).get();
        if (campaign.getInfluencerCountExpected() <= campaign.getInfluencerCountCurrent()) {
            return ApiResponse.sendError(400, "This campaign reached max the number of slots", request.getRequestURI());
        }

        if (accepted) {
            invitation.setStatus("ACCEPTED");
            invitationRepository.save(invitation);
            campaign.setInfluencerCountCurrent(campaign.getInfluencerCountCurrent() + 1);
            campaignRepository.save(campaign);
            ChatRoom chatRoom = chatRoomRepository.findById(invitation.getCampaignId()).get();
            chatRoom.getMembers().add(influencerId);
            chatRoomRepository.save(chatRoom);
        } else {
            invitation.setStatus("REJECTED");
            invitationRepository.save(invitation);
        }

        invitationRepository.save(invitation);

        return ApiResponse.sendSuccess(201, "Create invitation successfully", invitation, request.getRequestURI());
    }
}
