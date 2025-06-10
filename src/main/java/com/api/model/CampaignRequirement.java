package com.api.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

public class CampaignRequirement {

    private String imageUrl;
    private String postUrl;
    private String status;
    @CreatedDate
    private LocalDateTime uploadedAt;

    public CampaignRequirement() {
    }

    public CampaignRequirement(String imageUrl, String postUrl) {
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = "PENDING";
    }

    public CampaignRequirement(String imageUrl, String postUrl, String status) {
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = status;
    }

    public CampaignRequirement(String imageUrl, String postUrl, String status, LocalDateTime uploadedAt) {
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = status;
        this.uploadedAt = uploadedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

}
