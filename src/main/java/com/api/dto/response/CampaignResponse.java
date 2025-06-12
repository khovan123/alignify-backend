package com.api.dto.response;

import com.api.model.Campaign;
import com.api.model.Category;
import com.api.repository.CategoryRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class CampaignResponse {

    private String campaignId;
    private String brandId;
    private String content;
    private String imageUrl;
    private List<Category> categories;
    private LocalDateTime createdDate;
    private String status;
    private long budget;
    private Map<String, Integer> campaignRequirements;
    private List<String> influencerRequirement;
    private int influencerCount;

    public CampaignResponse() {
    }

    public CampaignResponse(Campaign campaign, CategoryRepository categoryRepository) {
        this.campaignId = campaign.getCampaignId();
        this.brandId = campaign.getBrandId();
        this.content = campaign.getContent();
        this.imageUrl = campaign.getImageUrl();
        this.categories = categoryRepository.findAllByCategoryIdIn(campaign.getCategoryIds());
        this.createdDate = campaign.getCreatedDate();
        this.status = campaign.getStatus();
        this.budget = campaign.getBudget();
        this.campaignRequirements = campaign.getCampaignRequirements();
        this.influencerRequirement = campaign.getInfluencerRequirement();
        this.influencerCount = campaign.getInfluencerCount();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public int getInfluencerCount() {
        return influencerCount;
    }

    public void setInfluencerCount(int influencerCount) {
        this.influencerCount = influencerCount;
    }

}
