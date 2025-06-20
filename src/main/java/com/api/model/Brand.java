package com.api.model;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "brands")
public class Brand{

    @Id
    private String userId;
    private String bio;
    private List<String> categoryIds;
    private Map<String, String> contacts;
    private Map<String, String> socialMediaLinks;
    private LocalDateTime establishDate;
    private int totalCampaign;

    @CreatedDate
    private LocalDateTime createdAt;

    public Brand() {
        this.totalCampaign = 0;
        this.categoryIds = new ArrayList<>();
        this.contacts = new HashMap<>();
        this.socialMediaLinks = new HashMap<>();
    }

    public Brand(String userId, String bio, List<String> categoryIds, Map<String, String> contacts, Map<String, String> socialMediaLinks, LocalDateTime establishDate, int totalCampaign, LocalDateTime createdAt) {
        this.userId = userId;
        this.bio = bio;
        this.categoryIds = categoryIds;
        this.contacts = contacts;
        this.socialMediaLinks = socialMediaLinks;
        this.establishDate = establishDate;
        this.totalCampaign = totalCampaign;
        this.createdAt = createdAt;
    }

    public int getTotalCampaign() {
        return totalCampaign;
    }

    public void setTotalCampaign(int totalCampaign) {
        this.totalCampaign = totalCampaign;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<String> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<String> categoryIds) {
        this.categoryIds = categoryIds;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    public Map<String, String> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(Map<String, String> socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }

    public LocalDateTime getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(LocalDateTime establishDate) {
        this.establishDate = establishDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
