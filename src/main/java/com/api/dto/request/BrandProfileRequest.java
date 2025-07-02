package com.api.dto.request;

import com.api.model.Contact;
import com.api.model.SocialMedia;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public class BrandProfileRequest {

    private String name;
    private String bio;
    private List<String> categoryIds;
    private List<Contact> contacts;
    private List<SocialMedia> socialMediaLinks;
    private ZonedDateTime establishDate;

    public BrandProfileRequest() {
    }

    public BrandProfileRequest(String name, String bio, List<String> categoryIds, List<Contact> contacts,
            List<SocialMedia> socialMediaLinks, ZonedDateTime establishDate) {
        this.name = name;
        this.bio = bio;
        this.categoryIds = categoryIds;
        this.contacts = contacts;
        this.socialMediaLinks = socialMediaLinks;
        this.establishDate = establishDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

}
