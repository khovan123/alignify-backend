package com.api.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.api.model.Campaign;
import com.api.model.Category;
import com.api.repository.BrandRepository;
import com.api.repository.CategoryRepository;
import com.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CampaignResponse {
    
     
    @Autowired
    private UserRepository userRepository;
    
    private String campaignId;
    private String brandName;
    private String content;
    private String imageUrl;
    private List<Category> categories;
    private LocalDateTime createdDate;
    private LocalDateTime endDate;
    private String status;
    private long budget;
    private Map<String, Integer> campaignRequirements;
    private List<String> influencerRequirement;
    private int influencerCountExpected;
    private int influencerCountCurrent;
    private int applicationTotal;

    public CampaignResponse() {
    }

    public CampaignResponse(Campaign campaign, CategoryRepository categoryRepository) {
        this.campaignId = campaign.getCampaignId();
        this.brandName = userRepository.findByUserId(campaign.getBrandId()).getName();
        this.content = campaign.getContent();
        this.imageUrl = campaign.getImageUrl();
        this.categories = categoryRepository.findAllByCategoryIdIn(campaign.getCategoryIds());
        this.createdDate = campaign.getCreatedDate();
        this.endDate = campaign.getEndDate();
        this.status = campaign.getStatus();
        this.budget = campaign.getBudget();
        this.campaignRequirements = campaign.getCampaignRequirements();
        this.influencerRequirement = campaign.getInfluencerRequirement();
        this.influencerCountExpected = campaign.getInfluencerCountExpected();
        this.influencerCountCurrent = campaign.getInfluencerCountCurrent();
        this.applicationTotal = campaign.getApplicationTotal();
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
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
