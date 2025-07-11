package com.api.dto.response;

import java.time.ZonedDateTime;

import com.api.model.Invitation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class InvitationResponse {
  private String invitationId;
  private String influencerId;
  private CampaignResponse campaign;
  private String message;
  private String status;
  private ZonedDateTime createdAt;

  public InvitationResponse(Invitation invitation, CampaignResponse campaignResponse) {
    this.invitationId = invitation.getInvitationId();
    this.influencerId = invitation.getInfluencerId();
    this.campaign = campaignResponse;
    this.message = invitation.getMessage();
    this.status = invitation.getStatus();
    this.createdAt = invitation.getCreatedAt();
  }
}
