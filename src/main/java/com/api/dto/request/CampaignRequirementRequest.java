package com.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public class CampaignRequirementRequest {

    @JsonProperty("index")
    private int index;
//    @JsonProperty("imageUrl")
//    private String imageUrl;
    @JsonProperty("image")
    private MultipartFile image;
    @JsonProperty("postUrl")
    private String postUrl;
    @JsonProperty("status")
    private String status;

    // public CampaignRequirementRequest(@JsonProperty("index") int index,
    // @JsonProperty("imageUrl") String imageUrl,
    // @JsonProperty("postUrl") String postUrl) {
    // this.index = index;
    // this.imageUrl = imageUrl;
    // this.postUrl = postUrl;
    // }
    public CampaignRequirementRequest(@JsonProperty("index") int index, @JsonProperty("image") MultipartFile image,
            @JsonProperty("postUrl") String postUrl, @JsonProperty("status") String status) {
        this.index = index;
        this.image = image;
        this.postUrl = postUrl;
        this.status = status;
    }
//    public CampaignRequirementRequest(@JsonProperty("index") int index, @JsonProperty("imageUrl") String imageUrl,
//            @JsonProperty("postUrl") String postUrl, @JsonProperty("status") String status) {
//        this.index = index;
//        this.imageUrl = imageUrl;
//        this.postUrl = postUrl;
//        this.status = status;
//    }

    // public CampaignRequirementRequest(@JsonProperty("status") String status) {
    // this.status = status;
    // }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

//    public String getImageUrl() {
//        return imageUrl;
//    }
//
//    public void setImageUrl(String imageUrl) {
//        this.imageUrl = imageUrl;
//    }
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
