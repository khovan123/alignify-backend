package com.api.dto.request;

import java.time.ZonedDateTime;
import java.util.List;

import com.api.model.SocialMedia;
import com.fasterxml.jackson.annotation.JsonProperty;

public class InfluencerProfileRequest {

    @JsonProperty("name")
    private String name;
    @JsonProperty("DoB")
    private ZonedDateTime DoB;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("bio")
    private String bio;
    @JsonProperty("socialMediaLinks")
    private List<SocialMedia> socialMediaLinks;
    @JsonProperty("categoryIds")
    private List<String> categoryIds;
    @JsonProperty("isPublic")
    private Boolean isPublic;

    public InfluencerProfileRequest(@JsonProperty("name") String name, @JsonProperty("DoB") ZonedDateTime DoB,
            @JsonProperty("gender") String gender, @JsonProperty("bio") String bio,
            @JsonProperty("socialMediaLinks") List<SocialMedia> socialMediaLinks,
            @JsonProperty("categoryIds") List<String> categoryIds, @JsonProperty("isPublic") Boolean isPublic) {
        this.name = name;
        this.DoB = DoB;
        this.gender = gender;
        this.bio = bio;
        this.socialMediaLinks = socialMediaLinks;
        this.categoryIds = categoryIds;
        this.isPublic = isPublic;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InfluencerProfileRequest() {
    }

    public ZonedDateTime getDoB() {
        return DoB;
    }

    public void setDoB(ZonedDateTime DoB) {
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

    public List<SocialMedia> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(List<SocialMedia> socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

}
