package com.api.model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private Map<String, List<CampaignRequirement>> campaignRequirementTrackings;
    private double process;
    private String status;

    @CreatedDate
    private ZonedDateTime createdAt;

    public CampaignTracking() {
        this.campaignRequirementTrackings = new HashMap<>();
        this.process = 0.0;
        this.status = "PENDING";
    }

    public CampaignTracking(String applicationId, String campaignId, String brandId, String influencerId,
            Map<String, Integer> campaignRequirement) {
        this.campaignTrackingId = applicationId;
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.influencerId = influencerId;
        this.process = 0.0;
        this.campaignRequirementTrackings = new HashMap<>();
        campaignRequirement.forEach((key, count) -> {
            List<CampaignRequirement> requirements = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                requirements.add(new CampaignRequirement(i));
            }
            campaignRequirementTrackings.put(key, requirements);
        });
        this.status = "PENDING";
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

    public Map<String, List<CampaignRequirement>> getCampaignRequirementTrackings() {
        return campaignRequirementTrackings;
    }

    public void setCampaignRequirementTrackings(Map<String, List<CampaignRequirement>> campaignRequirementTrackings) {
        this.campaignRequirementTrackings = campaignRequirementTrackings;
    }

    public double getProcess() {
        return process;
    }

    public void setProcess(double process) {
        this.process = process;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
