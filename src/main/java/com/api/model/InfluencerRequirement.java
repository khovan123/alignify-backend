package com.api.model;

public class InfluencerRequirement {
  private String platform;
  private int followers;

  public InfluencerRequirement() {

  }

  public InfluencerRequirement(String platform, int followers) {
    this.platform = platform;
    this.followers = followers;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }

  public void setFollowers(int followers) {
    this.followers = followers;
  }

  public String getPlatform() {
    return platform;
  }

  public int getFollowers() {
    return followers;
  }
}
