package com.api.dto.response;

import com.api.model.Application;
import com.api.model.Influencer;
import com.api.model.User;

public class ApplicationPlusInfluencer extends Application {

    private String influencerName;
    private String avatarUrl;
    private int follower;
    private double rating;

    public ApplicationPlusInfluencer() {
    }

    public ApplicationPlusInfluencer(Application application) {
        super(application.getApplicationId(), application.getCampaignId(), application.getInfluencerId(),
                application.getBrandId(), application.getLimited(), application.getStatus(),
                application.getCreatedAt());
    }

    public ApplicationPlusInfluencer(User user, Influencer influencer, Application application) {
        super(application.getApplicationId(), application.getCampaignId(), application.getInfluencerId(),
                application.getBrandId(), application.getLimited(), application.getStatus(),
                application.getCreatedAt());
        this.influencerName = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.follower = influencer.getFollower();
        this.rating = influencer.getRating();
    }

    public String getInfluencerName() {
        return influencerName;
    }

    public void setInfluencerName(String influencerName) {
        this.influencerName = influencerName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
