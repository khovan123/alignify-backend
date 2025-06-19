package com.api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("campaigns")
public class Campaign {

    @Id
    private String campaignId;
    private String brandId;
    private String campaignName;
    private String content;
    private String imageUrl;
    private List<String> categoryIds;
    @CreatedDate
    private LocalDateTime createdDate;
    private LocalDateTime endDate; 
    private String status;
    private long budget;
    private Map<String, Integer> campaignRequirements;
    private List<String> influencerRequirement;
    private int influencerCountExpected;
    private int influencerCountCurrent;
    private int applicationTotal;

    public Campaign() {
        this.categoryIds = new ArrayList<>();
        this.status = "DRAFT";
        this.influencerCountCurrent = 0;
        this.applicationTotal = 0;
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl, List<String> categoryIds,
            LocalDateTime createdDate, String status, long budget, Map<String, Integer> campaignRequirements,
            List<String> influencerRequirement, int influencerCountExpected) {
        this.campaignName = campaignName;
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.createdDate = createdDate;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirement = influencerRequirement;
        this.influencerCountExpected = influencerCountExpected;
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl, List<String> categoryIds,
            LocalDateTime createdDate, String status, long budget, Map<String, Integer> campaignRequirements,
            List<String> influencerRequirement, int influencerCountExpected, int influencerCountCurrent, int applicationTotal) {
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.campaignName = campaignName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.createdDate = createdDate;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirement = influencerRequirement;
        this.influencerCountExpected = influencerCountExpected;
        this.influencerCountCurrent = influencerCountCurrent;
        this.applicationTotal = applicationTotal;
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl, List<String> categoryIds, LocalDateTime createdDate, LocalDateTime endDate, String status, long budget, Map<String, Integer> campaignRequirements, List<String> influencerRequirement, int influencerCountExpected, int influencerCountCurrent, int applicationTotal) {
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.campaignName = campaignName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.createdDate = createdDate;
        this.endDate = endDate;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirement = influencerRequirement;
        this.influencerCountExpected = influencerCountExpected;
        this.influencerCountCurrent = influencerCountCurrent;
        this.applicationTotal = applicationTotal;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
    
    public String getCampaignName() {
        return campaignName;
    }
public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public Map<String, Integer> getCampaignRequirements() {
        return campaignRequirements;
    }

    public void setCampaignRequirements(Map<String, Integer> campaignRequirements) {
        this.campaignRequirements = campaignRequirements;
    }

    public List<String> getInfluencerRequirement() {
        return influencerRequirement;
    }

    public void setInfluencerRequirement(List<String> influencerRequirement) {
        this.influencerRequirement = influencerRequirement;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

}