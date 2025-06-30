package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

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
    private ZonedDateTime createdAt;

    public Application() {
        this.limited = 2;
        this.status = "PENDING";
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Application(String campaignId, String influencerId, String brandId) {
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.brandId = brandId;
        this.limited = 2;
        this.status = "PENDING";
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Application(String applicationId, String campaignId, String influencerId, String brandId, int limited,
            String status, ZonedDateTime createdAt) {
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

}
