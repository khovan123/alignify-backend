package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlatformRequirementDetailsTrackingRequest {

    @JsonProperty("platform")
    private String platform;
    @JsonProperty("post_type")
    private String post_type;
    @JsonProperty("index")
    private int index;
    @JsonProperty("postUrl")
    private String postUrl;
    @JsonProperty("status")
    private String status;

    public PlatformRequirementDetailsTrackingRequest(@JsonProperty("platform") String platform,
            @JsonProperty("post_type") String post_type, @JsonProperty("index") int index,
            @JsonProperty("postUrl") String postUrl, @JsonProperty("status") String status) {
        this.platform = platform;
        this.post_type = post_type;
        this.index = index;
        this.postUrl = postUrl;
        this.status = status;
    }

    public String getPlatform() {
        return platform;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
