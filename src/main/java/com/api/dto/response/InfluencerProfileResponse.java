package com.api.dto.response;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import com.api.model.Category;
import com.api.model.Influencer;
import com.api.model.SocialMedia;
import com.api.model.User;
import com.api.repository.CategoryRepository;

public class InfluencerProfileResponse {

    private String userId;
    private String name;
    private String email;
    private String roleId;
    private String avatarUrl;
    private String backgroundUrl;
    private ZonedDateTime DoB;
    private String gender;
    private String bio;
    private List<SocialMedia> socialMediaLinks;
    private double rating;
    private List<Category> categories;
    private int follower;
    private boolean isPublic;
    private int completedCampaign;

    public InfluencerProfileResponse(User user, Influencer influencer) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.follower = influencer.getFollower();
    }

    public InfluencerProfileResponse(User user, Influencer influencer, List<Category> allCategories ) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.roleId = user.getRoleId();
        this.avatarUrl = user.getAvatarUrl();
        this.backgroundUrl = user.getBackgroundUrl();
        this.bio = influencer.getBio();
        this.isPublic = influencer.isPublic();
        this.email = user.getEmail();
        this.DoB = influencer.getDoB();
        this.gender = influencer.getGender();
        this.bio = influencer.getBio();
        this.socialMediaLinks = influencer.getSocialMediaLinks();
        this.rating = influencer.getRating();
        this.categories = allCategories.stream()
                .filter(cat -> influencer.getCategoryIds().contains(cat.getCategoryId()))
                .toList();
        this.follower = influencer.getFollower();
    }

    public InfluencerProfileResponse(User user, Influencer influencer, CategoryRepository categoryRepository) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.roleId = user.getRoleId();
        this.avatarUrl = user.getAvatarUrl();
        this.backgroundUrl = user.getBackgroundUrl();
        this.bio = influencer.getBio();
        this.isPublic = influencer.isPublic();
        this.email = user.getEmail();
        this.DoB = influencer.getDoB();
        this.gender = influencer.getGender();
        this.bio = influencer.getBio();
        this.socialMediaLinks = influencer.getSocialMediaLinks();
        this.rating = influencer.getRating();
        this.categories = (influencer.getCategoryIds() != null && !influencer.getCategoryIds().contains(null))
                ? categoryRepository.findAllById(influencer.getCategoryIds())
                : Collections.emptyList();
        this.follower = influencer.getFollower();
    }

    public InfluencerProfileResponse(User user, Influencer influencer, int completedCampaign, boolean isOwner,
            CategoryRepository categoryRepository) {
        this.completedCampaign = completedCampaign;
        this.userId = user.getUserId();
        this.name = user.getName();
        this.roleId = user.getRoleId();
        this.avatarUrl = user.getAvatarUrl();
        this.backgroundUrl = user.getBackgroundUrl();
        this.bio = influencer.getBio();
        this.isPublic = influencer.isPublic();
        if (isPublic || isOwner) {
            this.email = user.getEmail();
            this.DoB = influencer.getDoB();
            this.gender = influencer.getGender();
            this.bio = influencer.getBio();
            this.socialMediaLinks = influencer.getSocialMediaLinks();
            this.rating = influencer.getRating();
            this.categories = (influencer.getCategoryIds() != null && !influencer.getCategoryIds().contains(null))
                    ? categoryRepository.findAllById(influencer.getCategoryIds())
                    : Collections.emptyList();
            this.follower = influencer.getFollower();
        }
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public int getCompletedCampaign() {
        return completedCampaign;
    }

    public void setCompletedCampaign(int completedCampaign) {
        this.completedCampaign = completedCampaign;
    }

}
