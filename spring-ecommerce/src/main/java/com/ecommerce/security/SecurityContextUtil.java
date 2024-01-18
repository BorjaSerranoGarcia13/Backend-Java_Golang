package com.ecommerce.security;

import com.ecommerce.exception.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class SecurityContextUtil {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    public static List<GrantedAuthority> getAuthorities() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof CustomAuthenticationToken) {
            return new ArrayList<>(((CustomAuthenticationToken) authentication).getAuthorities());
        }
        throw new AuthenticationException("User not authenticated");
    }

    public static Integer getUserId() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof CustomAuthenticationToken) {
            return ((CustomAuthenticationToken) authentication).getUserId();
        }
        throw new AuthenticationException("User not authenticated");
    }

    public static boolean isAdmin() {
        Authentication authentication = getAuthentication();
        if (authentication instanceof CustomAuthenticationToken) {
            return ((CustomAuthenticationToken) authentication).getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        }
        throw new AuthenticationException("User not authenticated");
    }
}