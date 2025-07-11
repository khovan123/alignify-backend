package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.ApiResponse;
import com.api.dto.request.InvitationRequest;
import com.api.dto.response.CampaignResponse;
import com.api.dto.response.InvitationResponse;
import com.api.model.Campaign;
import com.api.model.ChatRoom;
import com.api.model.Invitation;
import com.api.model.User;
import com.api.repository.CampaignRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.ChatRoomRepository;
import com.api.repository.InvitationRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/invitations")
public class InvitationController {

    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isCampaignOwner(#campaignId, authentication.principal) and @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal)")
    public ResponseEntity<?> sendInvatation(
            @PathVariable("campaignId") String campaignId,
            @RequestBody InvitationRequest invitationRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        if (!campaignId.equals(invitationRequest.getCampaignId())) {
            return ApiResponse.sendError(400, "Two check failed with id: " + campaignId, request.getRequestURI());
        }
        Campaign campaign = campaignRepository.findByCampaignIdAndBrandId(invitationRequest.getCampaignId(),
                brandId).get();
        if (campaign.getInfluencerCountExpected() <= campaign.getJoinedInfluencerIds().size()) {
            return ApiResponse.sendError(400, "Your campaign is enough Influencer", request.getRequestURI());
        }
        List<String> appliedInfluencerIds = campaign.getAppliedInfluencerIds();
        User user = userRepository.findByUserId(brandId).get();
        for (String influencerId : invitationRequest.getInfluencerIds()) {
            ChatRoom chatRoom = chatRoomRepository.findById(invitationRequest.getCampaignId()).get();
            if (!(invitationRepository.existsByCampaignIdAndInfluencerId(
                    invitationRequest.getCampaignId(),
                    influencerId) && chatRoom.getMembers().contains(influencerId))) {
                Invitation invitation = new Invitation(brandId, invitationRequest.getCampaignId(), influencerId,
                        invitationRequest.getMessage());
                invitationRepository.save(invitation);
                CampaignResponse campaignResponse = new CampaignResponse(user, campaign, categoryRepository);
                InvitationResponse invitationResponse = new InvitationResponse(invitation, campaignResponse);
                campaign.setApplicationTotal(campaign.getApplicationTotal() + 1);
                appliedInfluencerIds.add(influencerId);
                messagingTemplate.convertAndSend("/topic/users/" + influencerId + "/invitations", invitationResponse);
            }
        }
        campaign.setAppliedInfluencerIds(appliedInfluencerIds);
        campaignRepository.save(campaign);
        return ApiResponse.sendSuccess(201, "Send invitation successfully!", null, request.getRequestURI());
    }

    @PostMapping("/invitations/{invitationId}/confirm")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isJoinedInvitation(#invitationId, authentication.principal) and (@securityService.checkCampaignStatus(#campaignId,'PENDING',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'DRAFT',authentication.principal) or @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
    public ResponseEntity<?> confirmInvatation(
            @RequestParam("accepted") boolean accepted,
            @PathVariable("invitationId") String invitationId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        String influencerId = userDetails.getUserId();
        Invitation invitation = invitationRepository.findById(invitationId).get();
        Campaign campaign = campaignRepository
                .findByCampaignIdAndBrandId(invitation.getCampaignId(), invitation.getBrandId()).get();
        if (campaign.getInfluencerCountExpected() <= campaign.getJoinedInfluencerIds().size()) {
            invitation.setStatus("REJECTED");
            invitationRepository.save(invitation);
            return ApiResponse.sendError(400, "This campaign reached max the number of slots", request.getRequestURI());
        }

        if (accepted) {
            invitation.setStatus("ACCEPTED");
            invitationRepository.save(invitation);
            List<String> joinedInfluencerIds = campaign.getJoinedInfluencerIds();
            joinedInfluencerIds.add(influencerId);
            campaign.setJoinedInfluencerIds(joinedInfluencerIds);
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
