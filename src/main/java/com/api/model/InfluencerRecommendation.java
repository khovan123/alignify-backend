package com.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@Data
@ToString
@Getter
@Setter
public class InfluencerRecommendation {
    private String userId;
    private String name;
    private String avatarUrl;
    private String gender;
    private List<SocialMedia> socialMediaLinks;
    private double rating;
    private List<Category> categories;
    private int follower;

    @JsonCreator
    public InfluencerRecommendation(
            @JsonProperty("userId") String userId,
            @JsonProperty("name") String name,
            @JsonProperty("avatarUrl") String avatarUrl,
            @JsonProperty("gender") String gender,
            @JsonProperty("socialMediaLinks") List<SocialMedia> socialMediaLinks,
            @JsonProperty("rating") Double rating,
            @JsonProperty("categories") List<Category> categories,
            @JsonProperty("follower") Integer follower) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.gender = gender;
        this.socialMediaLinks = socialMediaLinks;
        this.rating = rating;
        this.categories = categories;
        this.follower = follower;
    }
}
