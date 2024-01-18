package com.ecommerce.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static com.ecommerce.constants.SessionAttributes.TOKEN;

public class CookieUtil {

    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public static String getTokenFromCookie() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return CookieUtil.getCookieValue(request, TOKEN);
    }

    public static void createAndSetTokenCookie(String token, HttpServletResponse response) {
        Cookie tokenCookie = new Cookie(TOKEN, token);
        tokenCookie.setSecure(true);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setPath("/");

        if (response != null) {
            response.addCookie(tokenCookie);
        } else {
            throw new IllegalStateException("Response object is null");
        }
    }

    public static void deleteTokenCookie(HttpServletResponse response) {
        String token = getTokenFromCookie();
        if (token != null) {
            Cookie tokenCookie = new Cookie(TOKEN, null);
            tokenCookie.setMaxAge(0);
            tokenCookie.setHttpOnly(true);
            tokenCookie.setPath("/");
            response.addCookie(tokenCookie);
        }
    }
}
