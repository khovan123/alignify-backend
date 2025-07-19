package com.api.model;

import lombok.*;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class PlatformRequirementDetailsTracking extends PlatformRequirementDetails {
    private String postUrl;
    private String status;
    private ZonedDateTime uploadedAt;

    public PlatformRequirementDetailsTracking(String postUrl, String post_type) {
        super(post_type);
        this.setRequirementDetailsTracking(postUrl);
    }

    public PlatformRequirementDetailsTracking() {
        this.status = null;
        this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public PlatformRequirementDetailsTracking(PlatformRequirementDetails platformRequirementDetails) {
        super(platformRequirementDetails.getPost_type());
        this.postUrl = null;
        this.status = null;
        this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

    public PlatformRequirementDetailsTracking(PlatformRequirementDetails platformRequirementDetails, String postUrl,
                                              String status, ZonedDateTime uploadedAt) {
        super(platformRequirementDetails.getPost_type());
        this.postUrl = postUrl;
        this.status = status;
        this.uploadedAt = uploadedAt;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatus(boolean accepted) {
        this.status = accepted ? "ACCEPTED" : "REJECTED";
    }

    public void setRequirementDetailsTracking(String postUrl) {
        this.postUrl = postUrl;
        this.status = "PENDING";
        this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
    }

}
