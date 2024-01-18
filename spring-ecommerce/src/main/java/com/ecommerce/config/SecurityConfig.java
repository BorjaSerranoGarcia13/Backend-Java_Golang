package com.ecommerce.config;

import com.ecommerce.constants.endpoints.api.ApiUserEndpointRoutes;
import com.ecommerce.constants.endpoints.web.HomeControllerWebEndpointRoutes;
import com.ecommerce.constants.endpoints.web.UserWebEndpointRoutes;
import com.ecommerce.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(UserWebEndpointRoutes.LOGIN, UserWebEndpointRoutes.AUTHENTICATE,
                                UserWebEndpointRoutes.LOGOUT, UserWebEndpointRoutes.CREATE, UserWebEndpointRoutes.SAVE,
                                HomeControllerWebEndpointRoutes.HOME, HomeControllerWebEndpointRoutes.PRODUCT_BY_ID).
                        permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(ApiUserEndpointRoutes.API_USER_LOGIN, ApiUserEndpointRoutes.API_USER_LOGOUT,
                                ApiUserEndpointRoutes.API_USER_CREATE).
                        permitAll()
                        .anyRequest().permitAll()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/users/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl(UserWebEndpointRoutes.LOGOUT)
                        .logoutSuccessUrl(UserWebEndpointRoutes.LOGIN)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        //http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}