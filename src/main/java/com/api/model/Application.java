package com.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "application")
public class Application {

    @Id
    private String applicationId;
    private String campaignId;
    private String influencerId;
    private int limited;
    private String status;
    @CreatedDate
    private LocalDateTime createdAt;

    public Application() {
        this.limited = 2;
        this.status = "PENDING";
    }

    public Application(String campaignId) {
        this.limited = 2;
        this.campaignId = campaignId;
        this.status = "PENDING";
    }

    public Application(String applicationId, String campaignId, String influencerId, int limited, String status, LocalDateTime createdAt) {
        this.applicationId = applicationId;
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.limited = limited;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
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

    public int getLimited() {
        return limited;
    }

    public void setLimited(int limited) {
        this.limited = limited;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
