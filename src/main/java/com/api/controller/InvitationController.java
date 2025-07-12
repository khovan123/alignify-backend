package com.api.controller;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.ApiResponse;
import com.api.dto.UserDTO;
import com.api.dto.request.InvitationRequest;
import com.api.dto.response.CampaignResponse;
import com.api.dto.response.InvitationResponse;
import com.api.model.Campaign;
import com.api.model.ChatMessage;
import com.api.model.ChatRoom;
import com.api.model.Invitation;
import com.api.model.User;
import com.api.repository.CampaignRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.ChatMessageRepository;
import com.api.repository.ChatRoomRepository;
import com.api.repository.InvitationRepository;
import com.api.repository.UserRepository;
import com.api.security.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1")
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
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/{campaignId}/invitations")
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
        User brand = userRepository.findByUserId(brandId).get();
        for (String influencerId : invitationRequest.getInfluencerIds()) {
            ChatRoom chatRoom = chatRoomRepository.findById(invitationRequest.getCampaignId()).get();
            if (!(invitationRepository.existsByCampaignIdAndInfluencerId(
                    invitationRequest.getCampaignId(),
                    influencerId) && chatRoom.getMembers().contains(influencerId))) {
                User influencer = userRepository.findByUserId(brandId).get();
                Invitation invitation = new Invitation(brandId, invitationRequest.getCampaignId(), influencerId,
                        invitationRequest.getMessage());
                invitationRepository.save(invitation);
                CampaignResponse campaignResponse = new CampaignResponse(brand, campaign,
                        categoryRepository);
                InvitationResponse invitationResponseForInfluencer = new InvitationResponse(invitation,
                        campaignResponse);
                UserDTO userDTO = new UserDTO(influencer);
                InvitationResponse invitationResponseForBrand = new InvitationResponse(
                        userDTO, invitation,
                        campaignResponse);
                campaign.setApplicationTotal(campaign.getApplicationTotal() + 1);
                appliedInfluencerIds.add(influencerId);
                messagingTemplate.convertAndSend("/topic/users/" + influencerId + "/invitations",
                        invitationResponseForInfluencer);
                messagingTemplate.convertAndSend("/topic/users/" + brandId + "/invitations",
                        invitationResponseForBrand);
            }
        }
        campaign.setAppliedInfluencerIds(appliedInfluencerIds);
        campaignRepository.save(campaign);
        return ApiResponse.sendSuccess(201, "Send invitation successfully!", null, request.getRequestURI());
    }

    @GetMapping("/invitations/influencer")
    @PreAuthorize("hasRole('ROLE_INFLUENCER')")
    public ResponseEntity<?> getAllInvitationsByInfluencer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        List<Invitation> invitations = invitationRepository.findAllByInfluencerId(userDetails.getUserId());
        List<InvitationResponse> invitationResponses = invitations.stream().map(invitation -> {
            User brand = userRepository.findByUserId(invitation.getBrandId()).get();
            Campaign campaign = campaignRepository.findByCampaignIdAndBrandId(invitation.getCampaignId(),
                    invitation.getBrandId()).get();
            CampaignResponse campaignResponse = new CampaignResponse(brand, campaign, categoryRepository);
            InvitationResponse invitationResponse = new InvitationResponse(invitation, campaignResponse);
            return invitationResponse;
        }).toList();
        return ApiResponse.sendSuccess(200, "Response successfully!", invitationResponses, request.getRequestURI());
    }

    @GetMapping("/invitations/brand")
    @PreAuthorize("hasRole('ROLE_BRAND')")
    public ResponseEntity<?> getAllInvitationsByBrand(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request) {
        User brand = userRepository.findByUserId(userDetails.getUserId()).get();
        List<Invitation> invitations = invitationRepository.findAllByBrandId(userDetails.getUserId());
        List<InvitationResponse> invitationResponses = invitations.stream().map(invitation -> {
            User influencer = userRepository.findByUserId(invitation.getInfluencerId()).get();
            Campaign campaign = campaignRepository.findByCampaignIdAndBrandId(invitation.getCampaignId(),
                    invitation.getBrandId()).get();
            UserDTO userDTO = new UserDTO(influencer);
            CampaignResponse campaignResponse = new CampaignResponse(brand, campaign, categoryRepository);
            InvitationResponse invitationResponse = new InvitationResponse(userDTO, invitation, campaignResponse);
            return invitationResponse;
        }).toList();
        return ApiResponse.sendSuccess(200, "Response successfully!", invitationResponses, request.getRequestURI());
    }

    @PostMapping("/invitations/{invitationId}/confirm")
    @PreAuthorize("hasRole('ROLE_INFLUENCER') and @securityService.isJoinedInvitation(#invitationId, authentication.principal) and @securityService.checkCampaignStatus(#campaignId,'RECRUITING',authentication.principal))")
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
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setChatRoomId(chatRoom.getChatRoomId());
            chatMessage.setMessage(userDetails.getUsername() + " đã vào phòng!");
            chatMessage.setUserId("#SYS");
            chatMessage.setName(userDetails.getUsername());
            chatMessage.setSendAt(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
            chatMessageRepository.save(chatMessage);
        } else {
            invitation.setStatus("REJECTED");
            invitationRepository.save(invitation);
        }

        invitationRepository.save(invitation);

        return ApiResponse.sendSuccess(201, "Create invitation successfully", invitation, request.getRequestURI());
    }
}
