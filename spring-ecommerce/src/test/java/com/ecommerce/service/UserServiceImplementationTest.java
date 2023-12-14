package com.ecommerce.service;

import com.ecommerce.constants.messages.UserErrorMessages;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;
import com.ecommerce.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplementationTest {

    @Mock
    private IUserRepository iUserRepository;

    private UserServiceImplementation userService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        userService = new UserServiceImplementation(iUserRepository);
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("username");
        when(iUserRepository.findByUsername("username")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("username");

        assertEquals(user, result);
        verify(iUserRepository, times(1)).findByUsername("username");
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("email@example.com");
        when(iUserRepository.findByEmail("email@example.com")).thenReturn(Optional.of(user));

        User result = userService.findByEmail("email@example.com");

        assertEquals(user, result);
        verify(iUserRepository, times(1)).findByEmail("email@example.com");
    }

    @Test
    public void testFindAll() {
        User user1 = new User();
        User user2 = new User();
        List<User> users = Arrays.asList(user1, user2);
        when(iUserRepository.findAll()).thenReturn(users);

        List<User> result = userService.findAll();

        assertEquals(users, result);
        verify(iUserRepository, times(1)).findAll();
    }

    @Test
    public void testFindById() {
        User user = new User();
        user.setId(1);
        when(iUserRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.findById(1);

        assertEquals(user, result);
        verify(iUserRepository, times(1)).findById(1);
    }

    @Test
    public void testSave() {
        User user = new User(1, "name", "username", "password", "example@example.com", "address", "123", true);

        when(iUserRepository.save(any(User.class))).thenReturn(user);

        User result = userService.save(user);

        assertEquals(user.getUsername(), result.getUsername());
        assertTrue(passwordEncoder.matches("password", result.getPassword()));
        verify(iUserRepository, times(1)).save(user);
    }

    @Test
    public void testDelete() {
        User user = new User();
        user.setId(1);
        user.setAdmin(true);
        when(iUserRepository.findById(1)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> userService.delete(1));
        verify(iUserRepository, times(1)).deleteById(1);
    }

    @Test
    public void testLogin_Success() {
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));

        when(iUserRepository.findByUsername("username")).thenReturn(Optional.of(user));

        User result = userService.login("username", "password");

        assertEquals(user, result);
    }

    @Test
    public void testLogin_Fail_WrongPassword() {
        User user = new User();
        user.setUsername("username");
        user.setPassword(passwordEncoder.encode("password"));

        when(iUserRepository.findByUsername("username")).thenReturn(Optional.of(user));

        assertThrows(UserException.class, () -> userService.login("username", "wrong-password"));
    }

    @Test
    public void testLogin_Fail_NoUsername() {
        assertThrows(UserException.class, () -> userService.login(null, "password"));
    }

    @Test
    public void testLogin_Fail_NoPassword() {
        assertThrows(UserException.class, () -> userService.login("username", null));
    }

    @Test
    public void testCheckUserExists() {
        User user = new User();
        user.setUsername("username");
        user.setEmail("email@example.com");
        user.setPhoneNumber("1234567890");

        User foundUser = new User();
        foundUser.setUsername("username");
        foundUser.setEmail("email@example.com");
        foundUser.setPhoneNumber("1234567890");

        when(iUserRepository.findByUsernameOrEmailOrPhoneNumber(user.getUsername(), user.getEmail(), user.getPhoneNumber()))
                .thenReturn(List.of(foundUser));

        List<String> result = userService.checkUserExists(user);

        assertEquals(3, result.size());
        assertEquals(UserErrorMessages.ERROR_USER_USERNAME_ALREADY_IN_USE, result.get(0));
        assertEquals(UserErrorMessages.ERROR_USER_EMAIL_ALREADY_IN_USE, result.get(1));
        assertEquals(UserErrorMessages.ERROR_USER_PHONE_NUMBER_ALREADY_IN_USE, result.get(2));
    }
}
