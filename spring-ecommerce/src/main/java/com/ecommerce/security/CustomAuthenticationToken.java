package com.ecommerce.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private final Integer userId;

    public CustomAuthenticationToken(String username, Integer userId, Collection<? extends
            GrantedAuthority> authorities) {
        super(username, null, authorities);
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "CustomAuthenticationToken{" +
                "username=" + super.getName() +
                ", userId=" + userId +
                ", authorities=" + super.getAuthorities() +
                '}';
    }
}
