package com.api.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@Data
@ToString
@Document(collection = "users")
public class User {

    @Id
    private String userId;
    private String name;
    private String email;
    private String password;
    private String roleId;
    private boolean isActive;
    private String avatarUrl;
    private String backgroundUrl;
    private ZonedDateTime createdAt;
    private List<String> permissionIds;
    private String userPlanId;
    private boolean twoFA;

    public User() {
        this.isActive = true;
        this.createdAt = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        this.permissionIds = new ArrayList<>();
        this.userPlanId = null;
        this.twoFA = false;
    }

    public User(String userId, String name, String email, String password, String roleId, boolean isActive,
            String avatarUrl, String backgroundUrl, ZonedDateTime createdAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.isActive = isActive;
        this.avatarUrl = avatarUrl;
        this.backgroundUrl = backgroundUrl;
        this.createdAt = createdAt;
        this.permissionIds = new ArrayList<>();
        this.userPlanId = null;
    }

}
