package com.api.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

public class CampaignRequirement {

    private int index;
    private String imageUrl;
    private String postUrl;
    private String status;
    @CreatedDate
    private LocalDateTime uploadedAt;

    public CampaignRequirement(int index) {
        this.index = index;
    }

    public CampaignRequirement(CampaignRequirement cr) {
        this.index = cr.index;
        this.imageUrl = cr.imageUrl;
        this.status = cr.status;
        this.postUrl = cr.postUrl;
    }

    public CampaignRequirement(int index, String imageUrl, String postUrl) {
        this.index = index;
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = "PENDING";
    }

    public CampaignRequirement(int index, String imageUrl, String postUrl, String status) {
        this.index = index;
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = status;
    }

    public CampaignRequirement(int index, String imageUrl, String postUrl, String status, LocalDateTime uploadedAt) {
        this.index = index;
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = status;
        this.uploadedAt = uploadedAt;
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