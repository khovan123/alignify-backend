package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@Document(collection = "campaignTrackings")
public class CampaignTracking {

    @Id
    private String campaignTrackingId;
    private String campaignId;
    private String brandId;
    private String influencerId;
    private List<PlatformRequirementTracking> platformRequirementTracking;
    private double process;
    private String status;
    private ZonedDateTime createdAt;

    public CampaignTracking() {
        this.platformRequirementTracking = new ArrayList<>();
        this.process = 0.0;
        this.status = "PENDING";
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public CampaignTracking(String applicationId, String campaignId, String brandId, String influencerId,
            List<PlatformRequirement> platformRequirement) {
        this.campaignTrackingId = applicationId;
        this.campaignId = campaignId;
        this.brandId = brandId;
        this.influencerId = influencerId;
        this.process = 0.0;
        this.platformRequirementTracking = new ArrayList<>();
        if (!platformRequirement.isEmpty()) {
            this.platformRequirementTracking = platformRequirement.stream().map(platform -> {
                return new PlatformRequirementTracking(platform);
            }).toList();
        }
        this.status = "PENDING";
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }
}
