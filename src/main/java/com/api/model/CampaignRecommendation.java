package com.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaignRecommendation {
    private String campaignId;
    private String brandName;
    private String brandAvatar;
    private String campaignName;
    private String imageUrl;
    private List<String> categoryName;
    private String reasonForMatch;
}