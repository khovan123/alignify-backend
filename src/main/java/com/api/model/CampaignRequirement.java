package com.api.model;

import java.time.LocalDateTime;

public class CampaignRequirement {

    private int index;
    private String imageUrl;
    private String postUrl;
    private String status;
    private LocalDateTime uploadedAt;

    public CampaignRequirement() {
        this.index = 0;
        this.imageUrl = null;
        this.postUrl = null;
        this.status = null;
        this.uploadedAt = LocalDateTime.now();
    }

    public CampaignRequirement(int index) {
        this.index = index;
        this.imageUrl = null;
        this.postUrl = null;
        this.status = null;
        this.uploadedAt = LocalDateTime.now();
    }

    public CampaignRequirement(CampaignRequirement cr) {
        this.index = cr.index;
        this.imageUrl = cr.imageUrl;
        this.status = cr.status;
        this.postUrl = cr.postUrl;
        this.status = cr.status;
        this.uploadedAt = cr.uploadedAt != null ? cr.uploadedAt : LocalDateTime.now();
    }

    public CampaignRequirement(int index, String imageUrl, String postUrl) {
        this.index = index;
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = "PENDING";
        this.uploadedAt = LocalDateTime.now();
    }

    public CampaignRequirement(String status) {
        this.status = status;
        this.uploadedAt = LocalDateTime.now();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
