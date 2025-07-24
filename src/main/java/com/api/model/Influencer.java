package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@ToString
@Document(collection = "influencers")
public class Influencer {

    @Id
    private String userId;
    private ZonedDateTime DoB;
    private String gender;
    private String bio;
    private List<SocialMedia> socialMediaLinks;
    private double rating;
    private List<String> categoryIds;
    private int follower;
    private boolean isPublic;
    private ZonedDateTime createdAt;

    public Influencer() {
        this.rating = 0.0;
        this.follower = 0;
        this.isPublic = true;
        this.gender = "NONE";
        this.categoryIds = new ArrayList<>();
        this.socialMediaLinks = new ArrayList<>();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Influencer(String userId, ZonedDateTime DoB, String gender, String bio, List<SocialMedia> socialMediaLinks,
            double rating, List<String> categoryIds, int follower, boolean isPublic, ZonedDateTime createdAt) {
        this.userId = userId;
        this.DoB = DoB;
        this.gender = gender;
        this.bio = bio;
        this.socialMediaLinks = socialMediaLinks;
        this.rating = rating;
        this.categoryIds = categoryIds;
        this.follower = follower;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }

}