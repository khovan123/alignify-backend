package com.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "applications")
public class Application {

    @Id
    private String applicationId;
    private String campaignId;
    private String influencerId;
    private String brandId;
    private int limited;
    private String status;
    @CreatedDate
    private LocalDateTime createdAt;

    public Application() {
        this.limited = 2;
        this.status = "PENDING";
    }
        
    
    public Application(String campaignId, String influencerId, String brandId) {
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.brandId = brandId;
        this.limited = 2;
        this.status = "PENDING";
    }

    public Application(String applicationId, String campaignId, String influencerId, String brandId, int limited,
            String status, LocalDateTime createdAt) {
        this.applicationId = applicationId;
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.brandId = brandId;
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

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

}
