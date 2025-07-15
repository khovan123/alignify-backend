package com.api.dto.response;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.api.model.Campaign;
import com.api.model.Category;
import com.api.model.InfluencerRequirement;
import com.api.model.PlatformRequirement;
import com.api.model.User;
import com.api.repository.CategoryRepository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
@Getter
@Setter
public class CampaignResponse {

    private String campaignId;
    private String brandId;
    private String brandName;
    private String brandAvartar;
    private String campaignName;
    private String content;
    private String imageUrl;
    private List<Category> categories;
    private ZonedDateTime createdAt;
    private ZonedDateTime dueAt;
    private ZonedDateTime startAt;
    private String status;
    private long budget;
    private List<PlatformRequirement> campaignRequirements;
    private List<InfluencerRequirement> influencerRequirements;
    private int influencerCountExpected;
    private List<String> joinedInfluencerIds;
    private int applicationTotal;
    private List<String> appliedInfluencerIds;
    private List<String> invitedInfluencerIds;

    public CampaignResponse() {
        this.joinedInfluencerIds = new ArrayList<>();
        this.campaignRequirements = new ArrayList<>();
        this.invitedInfluencerIds = new ArrayList<>();
        this.appliedInfluencerIds = new ArrayList<>();
        this.influencerRequirements = new ArrayList<>();
    }

    public CampaignResponse(User user, Campaign campaign, CategoryRepository categoryRepository) {
        this.campaignId = campaign.getCampaignId();
        this.brandId = campaign.getBrandId();
        this.brandName = user.getName();
        this.content = campaign.getContent();
        this.brandAvartar = user.getAvatarUrl();
        this.campaignName = campaign.getCampaignName();
        this.imageUrl = campaign.getImageUrl();
        this.categories = categoryRepository.findAllByCategoryIdIn(campaign.getCategoryIds());
        this.createdAt = campaign.getCreatedAt();
        this.dueAt = campaign.getDueAt();
        this.startAt = campaign.getStartAt();
        this.status = campaign.getStatus();
        this.budget = campaign.getBudget();
        this.campaignRequirements = campaign.getCampaignRequirements();
        this.influencerRequirements = campaign.getInfluencerRequirements();
        this.influencerCountExpected = campaign.getInfluencerCountExpected();
        this.joinedInfluencerIds = campaign.getJoinedInfluencerIds();
        this.applicationTotal = campaign.getApplicationTotal();
        this.appliedInfluencerIds = campaign.getAppliedInfluencerIds() != null ? campaign.getAppliedInfluencerIds()
                : List.of();
    }

    public CampaignResponse(User user, Campaign campaign, CategoryRepository categoryRepository,
            List<String> invitedInfluencerIds) {
        this.campaignId = campaign.getCampaignId();
        this.brandId = campaign.getBrandId();
        this.brandName = user.getName();
        this.content = campaign.getContent();
        this.brandAvartar = user.getAvatarUrl();
        this.campaignName = campaign.getCampaignName();
        this.imageUrl = campaign.getImageUrl();
        this.categories = categoryRepository.findAllByCategoryIdIn(campaign.getCategoryIds());
        this.createdAt = campaign.getCreatedAt();
        this.dueAt = campaign.getDueAt();
        this.startAt = campaign.getStartAt();
        this.status = campaign.getStatus();
        this.budget = campaign.getBudget();
        this.campaignRequirements = campaign.getCampaignRequirements();
        this.influencerRequirements = campaign.getInfluencerRequirements();
        this.influencerCountExpected = campaign.getInfluencerCountExpected();
        this.joinedInfluencerIds = campaign.getJoinedInfluencerIds();
        this.applicationTotal = campaign.getApplicationTotal();
        this.appliedInfluencerIds = campaign.getAppliedInfluencerIds() != null ? campaign.getAppliedInfluencerIds()
                : List.of();
        this.invitedInfluencerIds = invitedInfluencerIds;
    }
}
