package com.ecommerce.security;

import com.ecommerce.constants.endpoints.api.ApiUserEndpointRoutes;
import com.ecommerce.constants.endpoints.web.HomeControllerWebEndpointRoutes;
import com.ecommerce.constants.endpoints.web.UserWebEndpointRoutes;
import com.ecommerce.exception.AuthenticationException;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;
import com.ecommerce.utils.CookieUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ecommerce.constants.SessionAttributes.TOKEN;
import static com.ecommerce.utils.CookieUtil.deleteTokenCookie;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        if (path.contains("/vendor") || path.contains("/css") || path.contains("/images")) {
            return true;
        }

        List<String> apiRoutesWithoutAuthentication = List.of(ApiUserEndpointRoutes.API_USER_LOGIN,
                ApiUserEndpointRoutes.API_USER_LOGOUT, ApiUserEndpointRoutes.API_USER_CREATE);

        List<String> viewRoutesWithoutAuthentication = List.of(UserWebEndpointRoutes.LOGOUT, UserWebEndpointRoutes.CREATE,
                UserWebEndpointRoutes.LOGIN, UserWebEndpointRoutes.AUTHENTICATE, UserWebEndpointRoutes.SAVE);

        List<String> combinedRoutesWithoutAuthentication = Stream.concat(apiRoutesWithoutAuthentication.stream(), viewRoutesWithoutAuthentication.stream())
                .toList();

        return combinedRoutesWithoutAuthentication.contains(path);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            String token = extractTokenFromCookie(request);
            String path = request.getRequestURI();
            if (token != null && jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                User user = (User) this.userDetailsService.loadUserByUsername(username);
                Integer userIdFromToken = jwtUtil.extractUserId(token);

                if (user.getId().equals(userIdFromToken)) {
                    List<String> roles = jwtUtil.extractRoles(token);
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    CustomAuthenticationToken auth = new CustomAuthenticationToken(username, userIdFromToken,
                            authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    chain.doFilter(request, response);
                } else {
                    throw new AuthenticationException("Unauthorized access. Please log in again.");
                }
            } else {
                if (request.getRequestURI().startsWith("/home") ||
                        request.getRequestURI().startsWith(HomeControllerWebEndpointRoutes.PRODUCT_SEARCH) ||
                        request.getRequestURI().startsWith(HomeControllerWebEndpointRoutes.PURCHASE_CONFIRM) ||
                        request.getRequestURI().startsWith(HomeControllerWebEndpointRoutes.ORDER_SUMMARY) ||
                        request.getRequestURI().equals(HomeControllerWebEndpointRoutes.HOME)) {
                    deleteTokenCookie(response);
                    chain.doFilter(request, response);
                }
                throw new AuthenticationException("Unauthorized access. Please log in again.");
            }
        } catch (AuthenticationException | ExpiredJwtException | IllegalStateException | UserException e) {
            deleteTokenCookie(response);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        return CookieUtil.getCookieValue(request, TOKEN);
    }
}
