package com.api.model;

import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("admins")
public class Admin {

    @Id
    private String userId;
    private String name;
    private String email;
    private String password;
    private String roleId;

    @CreatedDate
    private Date createAt;

    public Admin() {
    }

    public Admin(String userId, String name, String email, String password, String roleId, Date createAt) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.createAt = createAt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

}
