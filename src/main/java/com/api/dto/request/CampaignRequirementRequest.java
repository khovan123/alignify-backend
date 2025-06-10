package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CampaignRequirementRequest {

    @JsonProperty("requirement")
    private String requirement;
    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("postUrl")
    private String postUrl;
    @JsonProperty("status")
    private String status;

    public CampaignRequirementRequest(@JsonProperty("requirement") String requirement, @JsonProperty("imageUrl") String imageUrl, @JsonProperty("postUrl") String postUrl, @JsonProperty("status") String status) {
        this.requirement = requirement;
        this.imageUrl = imageUrl;
        this.postUrl = postUrl;
        this.status = status;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
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

}
