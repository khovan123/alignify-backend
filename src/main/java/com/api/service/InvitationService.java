package com.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.api.dto.ApiResponse;
import com.api.dto.request.InvitationRequest;
import com.api.model.Invitation;
import com.api.repository.InvitationRepository;
import com.api.security.CustomUserDetails;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.servlet.http.HttpServletRequest;

@Service
@RequestMapping("/api/v1/invitations")
public class InvitationService {

  @Autowired
  private InvitationRepository invitationRepository;

  @PostMapping("")
  public ResponseEntity<?> createAndSentInvitation(@RequestBody InvitationRequest invitationRequest,
      @AuthenticationPrincipal CustomUserDetails userDetails,
      HttpServletRequest request) {
    List<String> influencerIds = invitationRequest.getInfluencerIds();
    String brandId = userDetails.getUserId();
    influencerIds.forEach(influencerId -> {
      Invitation invitation = new Invitation(brandId, influencerId, invitationRequest.getCampaignId(),
          invitationRequest.getMessage());
      invitationRepository.save(invitation);
    });
    return ApiResponse.sendSuccess(201, "Send invitation successfully!", "", request.getRequestURI());
  }
}
