package com.ecommerce.controller.restcontroller;

import com.ecommerce.dto.UserDto;
import com.ecommerce.model.User;
import com.ecommerce.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiUserControllerTest {

    @Mock
    private IUserService iUserService;

    private ApiUserController apiUserController;

    @BeforeEach
    void setUp() {
        apiUserController = new ApiUserController(iUserService);
    }

    @Test
    void getAllUsers() {
        User user = new User();
        UserDto userDto = new UserDto();
        List<User> users = List.of(user);
        List<UserDto> userDtos = List.of(userDto);

        when(iUserService.findAll()).thenReturn(users);
        when(iUserService.convertUserToDto(users)).thenReturn(userDtos);

        List<UserDto> result = apiUserController.getAllUsers();

        assertEquals(userDtos, result);
    }

    @Test
    void getAllUsersPaged() {
        UserDto userDto = new UserDto();
        Page<User> userPage = new PageImpl<>(List.of(new User()));
        when(iUserService.findAll(any(Pageable.class))).thenReturn(userPage);
        when(iUserService.convertUserToDto(any(User.class))).thenReturn(userDto);

        Page<UserDto> result = apiUserController.getAllUsersPaged(Pageable.unpaged());

        assertEquals(1, result.getTotalElements());
        assertEquals(userDto, result.getContent().get(0));
    }

    @Test
    void getUserById() {
        UserDto userDto = new UserDto();
        when(iUserService.findById(anyInt())).thenReturn(new User());
        when(iUserService.convertUserToDto(any(User.class))).thenReturn(userDto);

        UserDto result = apiUserController.getUserById(1);

        assertEquals(userDto, result);
    }

    @Test
    void login() {
        UserDto userDto = new UserDto();
        when(iUserService.login(anyString(), anyString())).thenReturn(new User());
        when(iUserService.convertUserToDto(any(User.class))).thenReturn(userDto);

        UserDto result = apiUserController.login("username", "password");

        assertEquals(userDto, result);
    }

    @Test
    void logout() {
        doNothing().when(iUserService).logout();

        apiUserController.logout();

        verify(iUserService, times(1)).logout();
    }

    @Test
    void saveUser() {
        User user = new User();
        UserDto userDto = new UserDto();
        when(iUserService.save(any(User.class))).thenReturn(user);
        when(iUserService.convertUserToDto(any(User.class))).thenReturn(userDto);

        UserDto result = apiUserController.saveUser(user);

        assertEquals(userDto, result);
    }

    @Test
    void deleteUserById() {
        doNothing().when(iUserService).delete(anyInt());

        apiUserController.deleteUserById(1);

        verify(iUserService, times(1)).delete(1);
    }

    @Test
    void getCurrentUserId() {
        when(iUserService.getCurrentUserId()).thenReturn(1);

        int result = apiUserController.getCurrentUserId();

        assertEquals(1, result);
    }

    @Test
    void isAdmin() {
        when(iUserService.isAdmin()).thenReturn(true);

        boolean result = apiUserController.isAdmin();

        assertTrue(result);
    }

    @Test
    void getTokenExpirationDate() {
        when(iUserService.getTokenExpirationDateInMinutes()).thenReturn(30L);

        String result = apiUserController.getTokenExpirationDate();

        assertEquals("Token expiration date: 30 minutes", result);
    }

}