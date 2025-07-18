package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@ToString
@Document(collection = "applications")
public class Application {

    @Id
    private String applicationId;
    private String campaignId;
    private String influencerId;
    private String cv_url;
    private String brandId;
    private int limited;
    private String status;
    private ZonedDateTime createdAt;

    public Application() {
        this.limited = 2;
        this.status = "PENDING";
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Application(String campaignId, String influencerId, String brandId, String cv_url) {
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.brandId = brandId;
        this.cv_url = cv_url;
        this.limited = 2;
        this.status = "PENDING";
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Application(String campaignId, String influencerId, String brandId) {
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.brandId = brandId;
        this.limited = 2;
        this.status = "PENDING";
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public Application(String applicationId, String campaignId, String influencerId, String cv_url, String brandId, int limited, String status, ZonedDateTime createdAt) {
        this.applicationId = applicationId;
        this.campaignId = campaignId;
        this.influencerId = influencerId;
        this.cv_url = cv_url;
        this.brandId = brandId;
        this.limited = limited;
        this.status = status;
        this.createdAt = createdAt;
    }
}
