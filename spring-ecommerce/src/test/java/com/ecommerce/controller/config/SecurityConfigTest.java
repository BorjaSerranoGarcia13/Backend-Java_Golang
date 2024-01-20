package com.ecommerce.controller.config;

import com.ecommerce.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class SecurityConfigTest {

    @Autowired
    private SecurityConfig securityConfig;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private String rawPassword;
    private String encodedPassword;

    @BeforeEach
    public void setUp() {
        rawPassword = "password";
        encodedPassword = bCryptPasswordEncoder.encode(rawPassword);
    }

    @Test
    public void testSecurityFilterChain() throws Exception {

    }

    @Test
    public void testPasswordEncoding() {
        assertThat(bCryptPasswordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }
}
