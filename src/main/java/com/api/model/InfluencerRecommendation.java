package com.api.model;

import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@Getter
@Setter
public class InfluencerRecommendation {
    private String userId;
    private String name;
    private String avatarUrl;
    private ZonedDateTime DoB;
    private String gender;
    private List<SocialMedia> socialMediaLinks;
    private double rating;
    private List<Category> categories;
    private int follower;
}
