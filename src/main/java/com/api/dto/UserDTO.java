package com.api.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.api.model.Permission;
import com.api.model.User;
import lombok.*;

@Getter
@Setter
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String userId;
    private String name;
    private String avatarUrl;
    private List<Permission> permissions;
    private ZonedDateTime createdAt;
    private boolean twoFA;

    public UserDTO(String userId, String name, String avatarUrl, List<Permission> permissions,
            ZonedDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.permissions = permissions;
    }

    public UserDTO(String userId, String name, String avatarUrl, List<Permission> permissions, boolean twoFA) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = null;
        this.permissions = permissions;
        this.twoFA = twoFA;
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

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.createdAt = null;
        this.permissions = new ArrayList<>();
    }
}
