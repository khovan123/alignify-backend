package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "invitations")
public class Invitation {

    @Id
    private String invitationId;
    private String brandId;
    private String campaignId;
    private String influencerId;
    private String message;
    private String status;
    private ZonedDateTime createdAt;

    public Invitation() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Invitation(String brandId, String campaignId, String influencerId, String message) {
        this.brandId = brandId;
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.message = message;
        this.status = "PENDING";
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Invitation(String invitationId, String brandId, String campaignId, String influencerId, String message,
            String status, ZonedDateTime createdAt) {
        this.invitationId = invitationId;
        this.brandId = brandId;
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(String invitationId) {
        this.invitationId = invitationId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getInfluencerId() {
        return influencerId;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
