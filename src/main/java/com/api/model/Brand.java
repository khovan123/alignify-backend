package com.api.model;

import java.util.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "brands")
public class Brand {

    @Id
    private String userId;
    private String avatarUrl;
    private String backgroundUrl;
    private String bio;
    private Map<String, String> contacts;
    private Map<String, String> socialMediaLinks;
    private Date establishDate;

    @CreatedDate
    private Date createAt;

    public Brand() {
        this.avatarUrl = "https://localhost:8080/cloud/avatar-default.png";
        this.backgroundUrl = "https://localhost:8080/cloud/avatar-default.png";
    }

    public Brand(String userId, String avatarUrl, String backgroundUrl, String bio, Map<String, String> contacts, Map<String, String> socialMediaLinks, Date establishDate, Date createAt) {
        this.userId = userId;
        this.avatarUrl = avatarUrl;
        this.backgroundUrl = backgroundUrl;
        this.bio = bio;
        this.contacts = contacts;
        this.socialMediaLinks = socialMediaLinks;
        this.establishDate = establishDate;
        this.createAt = createAt;
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

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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

    public Date getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(Date establishDate) {
        this.establishDate = establishDate;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

}
