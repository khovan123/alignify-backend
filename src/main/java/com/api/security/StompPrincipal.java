package com.api.security;

import java.security.Principal;

public class StompPrincipal implements Principal {
    private final String userId;
    private final String name;
    private final String roleId;
    private final String avatarUrl;

    public StompPrincipal(String userId, String name, String roleId, String avatarUrl) {
        this.userId = userId;
        this.name = name;
        this.roleId = roleId;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }
}