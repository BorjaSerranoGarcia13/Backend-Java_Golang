package com.ecommerce.model;

import com.ecommerce.config.SimpleGrantedAuthority;
import com.ecommerce.constants.messages.UserExceptionMessages;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    public void testSetId() {
        User user = new User();
        user.setId(1);
        assertEquals(1, user.getId());

        assertThrows(UserException.class, () -> user.setId(-1));
    }

    @Test
    public void testSetName() {
        User user = new User();
        user.setName("Test Name");
        assertEquals("Test Name", user.getName());

        assertThrows(UserException.class, () -> user.setName(null));
    }

    @Test
    public void testSetUsername() {
        User user = new User();
        user.setUsername("Test Username");
        assertEquals("Test Username", user.getUsername());

        assertThrows(UserException.class, () -> user.setUsername(null));
    }

    @Test
    public void testSetPassword() {
        User user = new User();
        user.setPassword("Test Password");
        assertEquals("Test Password", user.getPassword());

        assertThrows(UserException.class, () -> user.setPassword(null));
    }

    @Test
    public void testSetEmail() {
        User user = new User();
        user.setEmail("Test Email");
        assertEquals("Test Email", user.getEmail());

        assertThrows(UserException.class, () -> user.setEmail(null));
    }

    @Test
    public void testSetAddress() {
        User user = new User();
        user.setAddress("Test Address");
        assertEquals("Test Address", user.getAddress());

        assertThrows(UserException.class, () -> user.setAddress(null));
    }

    @Test
    public void testSetPhoneNumber() {
        User user = new User();
        user.setPhoneNumber("Test Phone Number");
        assertEquals("Test Phone Number", user.getPhoneNumber());

        assertThrows(UserException.class, () -> user.setPhoneNumber(null));
    }

    @Test
    public void testSetAuthorities() {
        User user = new User();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        user.setAuthorities(authorities);
        assertEquals(authorities, user.getAuthorities());

        assertThrows(UserException.class, () -> user.setAuthorities(null));
    }
}
