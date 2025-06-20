package com.api.dto.response;

import com.api.model.Brand;
import com.api.model.Category;
import com.api.model.User;
import com.api.repository.CategoryRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BrandProfileResponse {

    private String userId;
    private String name;
    private String email;
    private String roleId;
    private String avatarUrl;
    private String backgroundUrl;
    private String bio;
    private List<Category> categories;
    private Map<String, String> contacts;
    private Map<String, String> socialMediaLinks;
    private LocalDateTime establishDate;

    public BrandProfileResponse(User user, Brand brand, CategoryRepository categoryRepository) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.roleId = user.getRoleId();
        this.avatarUrl = user.getAvatarUrl();
        this.backgroundUrl = user.getBackgroundUrl();
        this.bio = brand.getBio();
        this.categories = (brand.getCategoryIds() != null && !brand.getCategoryIds().contains(null))
                ? categoryRepository.findAllById(brand.getCategoryIds())
                : Collections.emptyList();
        this.contacts = brand.getContacts();
        this.socialMediaLinks = brand.getSocialMediaLinks();
        this.establishDate = brand.getEstablishDate();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
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
