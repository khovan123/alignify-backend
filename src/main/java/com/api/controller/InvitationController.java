package com.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.api.model.Campaign;
import com.api.model.ChatRoom;
import com.api.model.Invitation;
import com.api.repository.CampaignRepository;
import com.api.repository.ChatRoomRepository;
import com.api.repository.InvitationRepository;
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

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_BRAND') and @securityService.isCampaignOwner(#campaignId, authentication.principal) and @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
    public ResponseEntity<?> sendInvatation(
            @RequestBody InvitationRequest invitationRequest,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        String brandId = userDetails.getUserId();
        Campaign campaign = campaignRepository.findByCampaignIdAndBrandId(invitationRequest.getCampaignId(),
                brandId).get();
        if (campaign.getInfluencerCountExpected() <= campaign.getJoinedInfluencerIds().size()) {
            return ApiResponse.sendError(400, "Your campaign is enough Influencer", request.getRequestURI());
        }
        for (String influencerId : invitationRequest.getInfluencerIds()) {
            ChatRoom chatRoom = chatRoomRepository.findById(invitationRequest.getCampaignId()).get();
            if (!(invitationRepository.existsByCampaignIdAndInfluencerId(
                    invitationRequest.getCampaignId(),
                    influencerId) && chatRoom.getMembers().contains(influencerId))) {
                Invitation invitation = new Invitation(brandId, influencerId, invitationRequest.getCampaignId(),
                        invitationRequest.getMessage());
                invitationRepository.save(invitation);
            }
        }
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
