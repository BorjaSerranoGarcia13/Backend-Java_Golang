package com.ecommerce.service;
import com.ecommerce.model.User;
import com.ecommerce.service.UserDetailsServiceImplementation;
import com.ecommerce.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import jakarta.servlet.http.HttpSession;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplementationTest {

    @Mock
    private IUserService iUserService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private HttpSession session;

    private UserDetailsServiceImplementation userDetailsService;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        userDetailsService = new UserDetailsServiceImplementation(iUserService, session, bCryptPasswordEncoder);
    }

    @Test
    public void testLoadUserByUsername() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setAdmin(false);

        when(iUserService.findByUsername("username")).thenReturn(user);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        UserDetails result = userDetailsService.loadUserByUsername("username");

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    public void testLoadUserByUsername_NullUsername() {
        assertThrows(IllegalArgumentException.class, () -> userDetailsService.loadUserByUsername(null));
    }

    @Test
    public void testLoadUserByUsername_EmptyUsername() {
        assertThrows(IllegalArgumentException.class, () -> userDetailsService.loadUserByUsername(""));
    }
}