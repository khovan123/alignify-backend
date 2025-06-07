package com.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BrandProfileRequest {

    private String bio;
    private List<String> categoryIds;
    private Map<String, String> contacts;
    private Map<String, String> socialMediaLinks;
    private LocalDateTime establishDate;

    public BrandProfileRequest() {
    }

    public BrandProfileRequest(String bio, List<String> categoryIds, Map<String, String> contacts, Map<String, String> socialMediaLinks, LocalDateTime establishDate) {
        this.bio = bio;
        this.categoryIds = categoryIds;
        this.contacts = contacts;
        this.socialMediaLinks = socialMediaLinks;
        this.establishDate = establishDate;
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

}
