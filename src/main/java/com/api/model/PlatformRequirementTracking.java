package com.api.model;

import java.util.ArrayList;
import java.util.List;

public class PlatformRequirementTracking {
  private String platform;
  private String post_type;
  private int quantity;
  private List<PlatformRequirementDetailsTracking> details;

  public PlatformRequirementTracking() {
    this.details = new ArrayList<>();
    this.quantity = 0;
  }

  public PlatformRequirementTracking(PlatformRequirement platformRequirement) {
    this.platform = platformRequirement.getPlatform();
    this.post_type = platformRequirement.getPost_type();
    this.quantity = platformRequirement.getQuantity();
    this.details = platformRequirement.getDetails().stream().map(pt -> {
      return new PlatformRequirementDetailsTracking(pt);
    }).toList();
  }

  public String getPlatform() {
    return platform;
  }

  public String getPost_type() {
    return post_type;
  }

  public int getQuantity() {
    return quantity;
  }

  public List<PlatformRequirementDetailsTracking> getDetails() {
    return details;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public void setPost_type(String post_type) {
    this.post_type = post_type;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public void setDetails(List<PlatformRequirementDetailsTracking> details) {
    this.details = details;
  }
}
