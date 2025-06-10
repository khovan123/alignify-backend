
package com.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("campaigns")
public class Campaign {
    @Id
    private String campaignId;
    private String userId;
    private String content;
    private String imageUrl;
    private List<String> categoryIds;
    @CreatedDate
    private Date timestamp;
    private boolean isPublic;
    private String status;
    private long budget;
    private Map<String,Integer> campaignRequirements;
    private List<String> influencerRequirement;
    public Campaign() {
        this.isPublic = true;
        this.categoryIds = new ArrayList<>();
        this.status = "DRAFT";
    }

    public Campaign(String campaignId, String userId, String content, String imageUrl, List<String> categoryIds, Date timestamp, boolean isPublic, String status) {
        this.campaignId = campaignId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.timestamp = timestamp;
        this.isPublic = isPublic;
        this.status = status;
    }

    public Campaign(String campaignId, String userId, String content, String imageUrl, List<String> categoryIds, Date timestamp, boolean isPublic, String status, long budget, Map<String, Integer> campaignRequirements, List<String> influencerRequirement) {
        this.campaignId = campaignId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.timestamp = timestamp;
        this.isPublic = isPublic;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirement = influencerRequirement;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
    
    
    
}
