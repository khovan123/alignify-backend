package com.api.model;

import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;
import org.springframework.data.annotation.CreatedDate;

@Document(collection = "influencers")
public class Influencer {

    @Id
    private String userId;
    private String avatarUrl;
    private String backgroundUrl;
    private Date DoB;
    private String gender;
    private String bio;
    private Map<String, String> socialMediaLinks;
    private double rating;
    private List<String> categoryIds;
    private List<String> followerIds;
    private boolean isPublic;

    @CreatedDate
    private LocalDateTime createdAt;

    public Influencer() {
        this.rating = 0.0;
        this.avatarUrl = "https://localhost:8080/cloud/avatar-default.png";
        this.backgroundUrl = "https://localhost:8080/cloud/avatar-default.png";
        this.isPublic = true;
        this.gender = "NONE";
    }

    public Influencer(String userId, String avatarUrl, String backgroundUrl, Date DoB, String gender, String bio, Map<String, String> socialMediaLinks, double rating, List<String> categoryIds, List<String> followerIds, boolean isPublic, LocalDateTime createdAt) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.backgroundUrl = backgroundUrl;
        this.DoB = DoB;
        this.gender = gender;
        this.bio = bio;
        this.socialMediaLinks = socialMediaLinks;
        this.rating = rating;
        this.categoryIds = categoryIds;
        this.followerIds = followerIds;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public Date getDoB() {
        return DoB;
    }

    public void setDoB(Date DoB) {
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public List<String> getFollowerIds() {
        return followerIds;
    }

    public void setFollowerIds(List<String> followerIds) {
        this.followerIds = followerIds;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
