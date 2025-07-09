package com.api.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.api.model.Permission;

public class UserDTO {

    private String userId;
    private String name;
    private String avatarUrl;
    private List<Permission> permissions;
    private ZonedDateTime createdAt;

    public UserDTO(String userId, String name, String avatarUrl, List<Permission> permissions,
            ZonedDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.permissions = permissions;
    }

    public UserDTO(String userId, String name, String avatarUrl, ZonedDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.permissions = new ArrayList<>();
    }

    public UserDTO(String userId, String name, String avatarUrl) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = null;
        this.permissions = new ArrayList<>();
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
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

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
