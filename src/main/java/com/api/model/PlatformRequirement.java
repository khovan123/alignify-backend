package com.api.model;

import java.util.ArrayList;
import java.util.List;

public class PlatformRequirement {
  private String platform;
  private String post_type;
  private int quantity;
  private List<PlatformRequirementDetails> details;

  public PlatformRequirement() {
    this.details = new ArrayList<>();
    this.quantity = 0;
  }

  public PlatformRequirement(String platform, String post_type, int quantity,
      List<PlatformRequirementDetails> details) {
    this.platform = platform;
    this.post_type = post_type;
    this.quantity = quantity;
    this.details = details;
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

  public List<PlatformRequirementDetails> getDetails() {
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

  public void setDetails(List<PlatformRequirementDetails> details) {
    this.details = details;
  }
}