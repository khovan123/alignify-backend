package com.api.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "campaignTrackings")
public class CampaignTracking {

    @Id
    private String campaignTrackingId;
    private String campaignId;
    private String brandId;
    private String influencerId;
    private Map<String, List<CampaignRequirement>> campaignRequirementTracking;
    private double process;

    @CreatedDate
    private LocalDateTime createdAt;

    public CampaignTracking() {
        this.campaignRequirementTracking = new HashMap<>();
        this.process = 0.0;
    }

    public CampaignTracking(String campaignTrackingId, String campaignId, String brandId, String influencerId, Map<String, List<CampaignRequirement>> campaignRequirementTracking, double process, LocalDateTime createdAt) {
        this.campaignTrackingId = campaignTrackingId;
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.influencerId = influencerId;
        this.campaignRequirementTracking = campaignRequirementTracking;
        this.process = process;
        this.createdAt = createdAt;
    }

    public String getCampaignTrackingId() {
        return campaignTrackingId;
    }

    public void setCampaignTrackingId(String campaignTrackingId) {
        this.campaignTrackingId = campaignTrackingId;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getInfluencerId() {
        return influencerId;
    }

    public void setInfluencerId(String influencerId) {
        this.influencerId = influencerId;
    }

    public Map<String, List<CampaignRequirement>> getCampaignRequirementTracking() {
        return campaignRequirementTracking;
    }

    public void setCampaignRequirementTracking(Map<String, List<CampaignRequirement>> campaignRequirementTracking) {
        this.campaignRequirementTracking = campaignRequirementTracking;
    }

    public double getProcess() {
        return process;
    }

    public void setProcess(double process) {
        this.process = process;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
