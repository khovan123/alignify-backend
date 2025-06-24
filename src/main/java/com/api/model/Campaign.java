package com.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document("campaigns")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Campaign {

    @Id
    private String campaignId;
    private String brandId;
    private String campaignName;
    private String content;
    private String imageUrl;
    private List<String> categoryIds;
    @CreatedDate
    private LocalDateTime createdAt;
    private LocalDateTime dueAt;
    private LocalDateTime startAt;
    private String status;
    private int budget;
    private Map<String, Integer> campaignRequirements;
    private List<String> influencerRequirements;
    private int influencerCountExpected;
    private int influencerCountCurrent;
    private int applicationTotal;
    private List<String> appliedInfluencerIds;

    public Campaign() {
        this.categoryIds = new ArrayList<>();
        this.status = "DRAFT";
        this.influencerCountCurrent = 0;
        this.applicationTotal = 0;
        this.appliedInfluencerIds = new ArrayList<>();
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl,
            List<String> categoryIds, String status, int budget, Map<String, Integer> campaignRequirements,
            List<String> influencerRequirements, int influencerCountExpected) {
        this.campaignName = campaignName;
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirements = influencerRequirements;
        this.influencerCountExpected = influencerCountExpected;
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl,
            List<String> categoryIds,
            LocalDateTime createdAt, String status, int budget, Map<String, Integer> campaignRequirements,
            List<String> influencerRequirements, int influencerCountExpected, int influencerCountCurrent,
            int applicationTotal) {
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.campaignName = campaignName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.createdAt = createdAt;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirements = influencerRequirements;
        this.influencerCountExpected = influencerCountExpected;
        this.influencerCountCurrent = influencerCountCurrent;
        this.applicationTotal = applicationTotal;
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl,
            List<String> categoryIds, LocalDateTime createdAt, LocalDateTime dueAt, LocalDateTime startAt,
            String status, int budget, Map<String, Integer> campaignRequirements, List<String> influencerRequirements,
            int influencerCountExpected, int influencerCountCurrent, int applicationTotal,
            List<String> appliedInfluencerIds) {
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.campaignName = campaignName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
        this.startAt = startAt;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirements = influencerRequirements;
        this.influencerCountExpected = influencerCountExpected;
        this.influencerCountCurrent = influencerCountCurrent;
        this.applicationTotal = applicationTotal;
        this.appliedInfluencerIds = appliedInfluencerIds != null ? appliedInfluencerIds : new ArrayList<>();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDueAt() {
        return dueAt;
    }

    public void setDueAt(LocalDateTime dueAt) {
        this.dueAt = dueAt;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public List<String> getInfluencerRequirements() {
        return influencerRequirements;
    }

    public void setInfluencerRequirements(List<String> influencerRequirements) {
        this.influencerRequirements = influencerRequirements;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Map<String, Integer> getCampaignRequirements() {
        return campaignRequirements;
    }

    public void setCampaignRequirements(Map<String, Integer> campaignRequirements) {
        this.campaignRequirements = campaignRequirements;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getInfluencerCountExpected() {
        return influencerCountExpected;
    }

    public void setInfluencerCountExpected(int influencerCountExpected) {
        this.influencerCountExpected = influencerCountExpected;
    }

    public int getInfluencerCountCurrent() {
        return influencerCountCurrent;
    }

    public void setInfluencerCountCurrent(int influencerCountCurrent) {
        this.influencerCountCurrent = influencerCountCurrent;
    }

    public int getApplicationTotal() {
        return applicationTotal;
    }

    public void setApplicationTotal(int applicationTotal) {
        this.applicationTotal = applicationTotal;
    }

    public List<String> getAppliedInfluencerIds() {
        return appliedInfluencerIds;
    }

    public void setAppliedInfluencerIds(List<String> appliedInfluencerIds) {
        this.appliedInfluencerIds = appliedInfluencerIds != null ? appliedInfluencerIds : new ArrayList<>();
    }

}
