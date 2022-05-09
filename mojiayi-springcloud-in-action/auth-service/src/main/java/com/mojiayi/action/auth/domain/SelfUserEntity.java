package com.mojiayi.action.auth.domain;

import java.util.List;

/**
 * @author mojiayi
 */
public class SelfUserEntity {
    private Long userId;
    private String username;
    private List<String> authorities;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }
}
