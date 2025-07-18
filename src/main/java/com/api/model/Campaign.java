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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document("campaigns")
@Getter
@Setter
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class Campaign {

    @Id
    private String campaignId;
    private String brandId;
    private String campaignName;
    private String content;
    private String imageUrl;
    private List<String> categoryIds;
    private ZonedDateTime createdAt;
    private ZonedDateTime dueAt;
    private ZonedDateTime startAt;
    private String status;
    private int budget;
    private List<PlatformRequirement> campaignRequirements;
    private List<InfluencerRequirement> influencerRequirements;
    private int influencerCountExpected;
    private List<String> joinedInfluencerIds;
    private int applicationTotal;
    private List<String> appliedInfluencerIds;
    private String contractUrl;

    public Campaign() {
        this.categoryIds = new ArrayList<>();
        this.status = "DRAFT";
        this.joinedInfluencerIds = new ArrayList<>();
        this.applicationTotal = 0;
        this.appliedInfluencerIds = new ArrayList<>();
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        contractUrl = null;
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl,
                    List<String> categoryIds, String status, int budget,
                    List<PlatformRequirement> campaignRequirements,
                    List<InfluencerRequirement> influencerRequirements, int influencerCountExpected) {
        this.campaignName = campaignName;
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirements = influencerRequirements;
        this.influencerCountExpected = influencerCountExpected;
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl,
                    List<String> categoryIds,
                    ZonedDateTime createdAt, String status, int budget,
                    List<PlatformRequirement> campaignRequirements,
                    List<InfluencerRequirement> influencerRequirements, int influencerCountExpected,
                    List<String> joinedInfluencerIds,
                    int applicationTotal) {
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.campaignName = campaignName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.createdAt = createdAt;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirements = influencerRequirements;
        this.influencerCountExpected = influencerCountExpected;
        this.joinedInfluencerIds = joinedInfluencerIds;
        this.applicationTotal = applicationTotal;
    }

    public Campaign(String campaignId, String brandId, String campaignName, String content, String imageUrl,
                    List<String> categoryIds, ZonedDateTime createdAt, ZonedDateTime dueAt, ZonedDateTime startAt,
                    String status, int budget,
                    List<PlatformRequirement> campaignRequirements,
                    List<InfluencerRequirement> influencerRequirements,
                    int influencerCountExpected,
                    List<String> joinedInfluencerIds, int applicationTotal,
                    List<String> appliedInfluencerIds) {
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.campaignName = campaignName;
        this.content = content;
        this.imageUrl = imageUrl;
        this.categoryIds = categoryIds;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
        this.startAt = startAt;
        this.status = status;
        this.budget = budget;
        this.campaignRequirements = campaignRequirements;
        this.influencerRequirements = influencerRequirements;
        this.influencerCountExpected = influencerCountExpected;
        this.joinedInfluencerIds = joinedInfluencerIds;
        this.applicationTotal = applicationTotal;
        this.appliedInfluencerIds = appliedInfluencerIds != null ? appliedInfluencerIds : new ArrayList<>();
    }

}
