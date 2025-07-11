package com.api.dto.response;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.api.model.Campaign;
import com.api.model.Category;
import com.api.model.InfluencerRequirement;
import com.api.model.PlatformRequirement;
import com.api.model.User;
import com.api.repository.CategoryRepository;

public class CampaignResponse {

    private String campaignId;
    private String brandId;
    private String brandName;
    private String brandAvartar;
    private String campaignName;
    private String content;
    private String imageUrl;
    private List<Category> categories;
    private ZonedDateTime createdAt;
    private ZonedDateTime dueAt;
    private ZonedDateTime startAt;
    private String status;
    private long budget;
    private List<PlatformRequirement> campaignRequirements;
    private List<InfluencerRequirement> influencerRequirements;
    private int influencerCountExpected;
    private List<String> joinedInfluencerIds;
    private int applicationTotal;
    private List<String> appliedInfluencerIds;

    public CampaignResponse() {
    }

    public CampaignResponse(User user, Campaign campaign, CategoryRepository categoryRepository) {
        this.campaignId = campaign.getCampaignId();
        this.brandId = campaign.getBrandId();
        this.brandName = user.getName();
        this.content = campaign.getContent();
        this.brandAvartar = user.getAvatarUrl();
        this.campaignName = campaign.getCampaignName();
        this.imageUrl = campaign.getImageUrl();
        this.categories = categoryRepository.findAllByCategoryIdIn(campaign.getCategoryIds());
        this.createdAt = campaign.getCreatedAt();
        this.dueAt = campaign.getDueAt();
        this.startAt = campaign.getStartAt();
        this.status = campaign.getStatus();
        this.budget = campaign.getBudget();
        this.campaignRequirements = campaign.getCampaignRequirements();
        this.influencerRequirements = campaign.getInfluencerRequirements();
        this.influencerCountExpected = campaign.getInfluencerCountExpected();
        this.joinedInfluencerIds = campaign.getJoinedInfluencerIds();
        this.applicationTotal = campaign.getApplicationTotal();
        this.appliedInfluencerIds = campaign.getAppliedInfluencerIds() != null ? campaign.getAppliedInfluencerIds()
                : List.of();
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBrandAvartar() {
        return brandAvartar;
    }

    public void setBrandAvartar(String brandAvartar) {
        this.brandAvartar = brandAvartar;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getDueAt() {
        return dueAt;
    }

    public void setDueAt(ZonedDateTime dueAt) {
        this.dueAt = dueAt;
    }

    public ZonedDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(ZonedDateTime startAt) {
        this.startAt = startAt;
    }

    public List<InfluencerRequirement> getInfluencerRequirements() {
        return influencerRequirements;
    }

    public void setInfluencerRequirements(List<InfluencerRequirement> influencerRequirements) {
        this.influencerRequirements = influencerRequirements;
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

    public List<PlatformRequirement> getCampaignRequirements() {
        return campaignRequirements;
    }

    public void setCampaignRequirements(List<PlatformRequirement> campaignRequirements) {
        this.campaignRequirements = campaignRequirements;
    }

    public int getInfluencerCountExpected() {
        return influencerCountExpected;
    }

    public void setInfluencerCountExpected(int influencerCountExpected) {
        this.influencerCountExpected = influencerCountExpected;
    }

    public List<String> getJoinedInfluencerIds() {
        return joinedInfluencerIds;
    }

    public void setJoinedInfluencerIds(List<String> joinedInfluencerIds) {
        this.joinedInfluencerIds = joinedInfluencerIds;
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
