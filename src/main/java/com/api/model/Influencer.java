package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.*;

@Document(collection = "influencers")
public class Influencer {

    @Id
    private String userId;
    private ZonedDateTime DoB;
    private String gender;
    private String bio;
    private List<SocialMedia> socialMediaLinks;
    private double rating;
    private List<String> categoryIds;
    private int follower;
    private boolean isPublic;
    private ZonedDateTime createdAt;

    public Influencer() {
        this.rating = 0.0;
        this.follower = 0;
        this.isPublic = true;
        this.gender = "NONE";
        this.categoryIds = new ArrayList<>();
        this.socialMediaLinks = new ArrayList<>();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Influencer(String userId, ZonedDateTime DoB, String gender, String bio, List<SocialMedia> socialMediaLinks, double rating, List<String> categoryIds, int follower, boolean isPublic, ZonedDateTime createdAt) {
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

    public List<SocialMedia> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(List<SocialMedia> socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
