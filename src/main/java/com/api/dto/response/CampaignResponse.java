
package com.api.dto.response;

import java.util.Date;
import java.util.List;
import java.util.Map;


public class CampaignResponse {
      private String campaignId;
    private String userId;
    private String content;
    private String imageUrl;
    private List<Map<String, String>> categories;
    private Date timestamp;
    private boolean isPublic;
    private String status;

    public CampaignResponse() {
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

    public List<Map<String, String>> getCategories() {
        return categories;
    }

    public void setCategories(List<Map<String, String>> categories) {
        this.categories = categories;
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
