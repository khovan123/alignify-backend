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
    private LocalDateTime DoB;
    private String gender;
    private String bio;
    private Map<String, String> socialMediaLinks;
    private double rating;
    private List<String> categoryIds;
    private int follower;
    private boolean isPublic;

    @CreatedDate
    private LocalDateTime createdAt;

    public Influencer() {
        this.rating = 0.0;
        this.follower = 0;
        this.isPublic = true;
        this.gender = "NONE";
        this.categoryIds = new ArrayList<>();
        this.socialMediaLinks = new HashMap<>();
    }

    public Influencer(String userId, LocalDateTime DoB, String gender, String bio, Map<String, String> socialMediaLinks, double rating, List<String> categoryIds, int follower, boolean isPublic, LocalDateTime createdAt) {
        this.userId = userId;

        this.DoB = DoB;
        this.gender = gender;
        this.bio = bio;
        this.socialMediaLinks = socialMediaLinks;
        this.rating = rating;
        this.categoryIds = categoryIds;
        this.follower = follower;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
