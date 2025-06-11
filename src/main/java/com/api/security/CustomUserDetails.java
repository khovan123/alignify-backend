package com.api.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.api.config.EnvConfig;

public class CustomUserDetails implements UserDetails {

    private final String userId;
    private final String roleId;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public CustomUserDetails(String userId, String roleId, String username, String password) {
        this.userId = userId;
        this.roleId = roleId;
        this.username = username;
        this.password = password;
        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(mapRoleIdToAuthority(roleId)));
    }

    private static String mapRoleIdToAuthority(String roleId) {
        if (roleId.equals(EnvConfig.ADMIN_ROLE_ID)) {
            return "ROLE_ADMIN";
        } else if (roleId.equals(EnvConfig.BRAND_ROLE_ID)) {
            return "ROLE_BRAND";
        } else if (roleId.equals(EnvConfig.INFLUENCER_ROLE_ID)) {
            return "ROLE_INFLUENCER";
        } else {
            return "ROLE_UNKNOWN";
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getRoleId() {
        return roleId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
