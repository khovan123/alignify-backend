package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "brands")
public class Brand {

    @Id
    private String userId;
    private String bio;
    private List<String> categoryIds;
    private List<Contact> contacts;
    private List<SocialMedia> socialMediaLinks;
    private ZonedDateTime establishDate;
    private int totalCampaign;
    private ZonedDateTime createdAt;

    public Brand() {
        this.totalCampaign = 0;
        this.categoryIds = new ArrayList<>();
        this.contacts = new ArrayList<>();
        this.socialMediaLinks = new ArrayList<>();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Brand(String userId, String bio, List<String> categoryIds, List<Contact> contacts,
            List<SocialMedia> socialMediaLinks, ZonedDateTime establishDate, int totalCampaign,
            ZonedDateTime createdAt) {
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

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<SocialMedia> getSocialMediaLinks() {
        return socialMediaLinks;
    }

    public void setSocialMediaLinks(List<SocialMedia> socialMediaLinks) {
        this.socialMediaLinks = socialMediaLinks;
    }

    public ZonedDateTime getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(ZonedDateTime establishDate) {
        this.establishDate = establishDate;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
