package com.api.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class InfluencerProfileRequest {

    private LocalDateTime DoB;
    private String gender;
    private String bio;
    private Map<String, String> socialMediaLinks;
    private List<String> categoryIds;

    public InfluencerProfileRequest( LocalDateTime DoB, String gender, String bio, Map<String, String> socialMediaLinks, List<String> categoryIds) {
        this.DoB = DoB;
        this.gender = gender;
        this.bio = bio;
        this.socialMediaLinks = socialMediaLinks;
        this.categoryIds = categoryIds;
    }

    public InfluencerProfileRequest() {
    }

    public LocalDateTime getDoB() {
        return DoB;
    }

    public void setDoB(LocalDateTime DoB) {
        this.DoB = DoB;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Map<String, String> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(Map<String, String> socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

}
