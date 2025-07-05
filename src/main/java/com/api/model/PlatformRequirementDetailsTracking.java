package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PlatformRequirementDetailsTracking extends PlatformRequirementDetails {
  private String postUrl;
  private String status;
  private ZonedDateTime uploadedAt;

  public PlatformRequirementDetailsTracking() {
    this.status = null;
    this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
  }

  public PlatformRequirementDetailsTracking(PlatformRequirementDetails platformRequirementDetails) {
    super(platformRequirementDetails.getPost_type(), platformRequirementDetails.getLike(),
        platformRequirementDetails.getComment(), platformRequirementDetails.getShare());
    this.postUrl = null;
    this.status = null;
    this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
  }

  public PlatformRequirementDetailsTracking(PlatformRequirementDetails platformRequirementDetails, String postUrl,
      String status, ZonedDateTime uploadedAt) {
    super(platformRequirementDetails.getPost_type(), platformRequirementDetails.getLike(),
        platformRequirementDetails.getComment(), platformRequirementDetails.getShare());
    this.postUrl = postUrl;
    this.status = status;
    this.uploadedAt = uploadedAt;
  }

  public String getPostUrl() {

    return this.postUrl;
  }

  public void setPostUrl(String postUrl) {
    this.postUrl = postUrl;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public ZonedDateTime getUploadedAt() {
    return this.uploadedAt;
  }

  public void setUploadedAt(ZonedDateTime uploadedAt) {
    this.uploadedAt = uploadedAt;
  }

  public void setRequirementDetailsTracking(String postUrl) {
    this.postUrl = postUrl;
    this.status = "PENDING";
    this.uploadedAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
  }

}
