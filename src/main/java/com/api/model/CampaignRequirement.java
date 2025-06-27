package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class CampaignRequirement {

    private int index;
    private String imageUrl;
    private String postUrl;
    private String status;
    private ZonedDateTime uploadedAt;

    public CampaignRequirement() {
        this.index = 0;
        this.imageUrl = null;
        this.postUrl = null;
        this.status = null;
        this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public CampaignRequirement(int index) {
        this.index = index;
        this.imageUrl = null;
        this.postUrl = null;
        this.status = null;
        this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public CampaignRequirement(CampaignRequirement cr) {
        this.index = cr.index;
        this.imageUrl = cr.imageUrl;
        this.status = cr.status;
        this.postUrl = cr.postUrl;
        this.status = cr.status;
        this.uploadedAt = cr.uploadedAt != null ? cr.uploadedAt : ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public CampaignRequirement(int index, String imageUrl, String postUrl) {
        this.index = index;
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = "PENDING";
        this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public CampaignRequirement(String status) {
        this.status = status;
        this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
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

    public ZonedDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(ZonedDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

}
