package com.api.model;

public class SocialMedia {

    private String platform;
    private String url;
    private int follower;

    public SocialMedia() {
    }

    public SocialMedia(String url, int follower) {
        this.url = url;
        this.follower = follower;
    }

    public SocialMedia(String platform, String url, int follower) {
        this.platform = platform;
        this.url = url;
        this.follower = follower;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }
}
