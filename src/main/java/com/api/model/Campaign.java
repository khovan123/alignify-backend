
package com.api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    public Campaign() {
        this.isPublic = true;
        this.categoryIds = new ArrayList<>();
    }

    
    public Campaign(String campaignId, String userId, String content, String imageUrl, List<String> categoryIds, Date timestamp, boolean isPublic) {
        this.campaignId = campaignId;
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.timestamp = timestamp;
        this.isPublic = isPublic;
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
    
    
}
