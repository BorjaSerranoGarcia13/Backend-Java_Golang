package com.ecommerce.utils;
import com.ecommerce.model.User;
import com.ecommerce.service.IUserService;
import com.ecommerce.utils.UserSessionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import jakarta.servlet.http.HttpSession;
import java.util.Optional;

import static com.ecommerce.constants.SessionAttributes.USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserSessionUtilsTest {

    @Mock
    private IUserService iUserService;

    @Mock
    private HttpSession session;

    private UserSessionUtils userSessionUtils;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        userSessionUtils = new UserSessionUtils(iUserService);
    }

    @Test
    public void testCheckVerifiedAdminFromSession_AdminUser() {
        User user = new User();
        user.setAdmin(true);

        when(session.getAttribute(USER_ID)).thenReturn(1);
        when(iUserService.findById(1)).thenReturn(user);

        Optional<User> result = userSessionUtils.checkVerifiedAdminFromSession(session);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testCheckVerifiedAdminFromSession_NonAdminUser() {
        User user = new User();
        user.setAdmin(false);

        when(session.getAttribute(USER_ID)).thenReturn(1);
        when(iUserService.findById(1)).thenReturn(user);

        Optional<User> result = userSessionUtils.checkVerifiedAdminFromSession(session);

        assertFalse(result.isPresent());
    }

    @Test
    public void testCheckVerifiedUserFromSession_ValidUser() {
        User user = new User();

        when(session.getAttribute(USER_ID)).thenReturn(1);
        when(iUserService.findById(1)).thenReturn(user);

        Optional<User> result = userSessionUtils.checkVerifiedUserFromSession(session);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }

    @Test
    public void testCheckVerifiedUserFromSession_NoUserInSession() {
        when(session.getAttribute(USER_ID)).thenReturn(null);

        Optional<User> result = userSessionUtils.checkVerifiedUserFromSession(session);

        assertFalse(result.isPresent());
    }
}
