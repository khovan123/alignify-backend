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
@NoArgsConstructor
public class UserDTO {

    private String userId;
    private String name;
    private String avatarUrl;
    private List<Permission> permissions;
    private ZonedDateTime createdAt;
    private boolean twoFA;
    private String email;
    private boolean sound;
    private boolean publicAcc;
    private boolean active;

    public UserDTO(String userId, String name, String avatarUrl, List<Permission> permissions,
            ZonedDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
        this.permissions = permissions;
    }

    public UserDTO(String userId, String name, String avatarUrl, boolean twoFA, String email) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.twoFA = twoFA;
        this.email = email;
    }

    public UserDTO(String userId, String name, String avatarUrl, List<Permission> permissions, boolean twoFA, boolean sound, boolean active) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.permissions = permissions;
        this.twoFA = twoFA;
        this.sound = sound;
        this.active = active;
    }

    public UserDTO(String userId, String name, String avatarUrl, List<Permission> permissions, boolean twoFA, boolean sound, boolean publicAcc, boolean active) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.permissions = permissions;
        this.twoFA = twoFA;
        this.sound = sound;
        this.publicAcc = publicAcc;
        this.active = active;
    }

    public UserDTO(String userId, String name, String avatarUrl, ZonedDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = createdAt;
    }

    public UserDTO(String userId, String name, String avatarUrl) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.createdAt = null;
    }

    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.avatarUrl = user.getAvatarUrl();
        this.createdAt = null;
    }
}
