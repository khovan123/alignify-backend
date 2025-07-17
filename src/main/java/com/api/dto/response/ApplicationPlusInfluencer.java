package com.api.dto.response;

import com.api.model.Application;
import com.api.model.Influencer;
import com.api.model.User;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApplicationPlusInfluencer extends Application {

    private String influencerName;
    private String avatarUrl;
    private int follower;
    private double rating;

    public ApplicationPlusInfluencer() {
    }

    public ApplicationPlusInfluencer(Application application) {
        super(application.getApplicationId(), application.getCampaignId(), application.getInfluencerId(),application.getCv_url(),
                application.getBrandId(), application.getLimited(), application.getStatus(),
                application.getCreatedAt());
    }

    public ApplicationPlusInfluencer(User user, Influencer influencer, Application application) {
        super(application.getApplicationId(), application.getCampaignId(), application.getInfluencerId(),application.getCv_url(),
                application.getBrandId(), application.getLimited(), application.getStatus(),
                application.getCreatedAt());
        this.influencerName = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.follower = influencer.getFollower();
        this.rating = influencer.getRating();
    }
}
