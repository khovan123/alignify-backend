package com.api.dto;

public class UserDTO {

    private String userId;
    private String name;
    private String avatarUrl;

    public UserDTO(String userId, String name, String avatarUrl) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
