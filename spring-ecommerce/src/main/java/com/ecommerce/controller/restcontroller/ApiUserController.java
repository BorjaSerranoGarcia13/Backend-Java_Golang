package com.ecommerce.controller.restcontroller;

import com.ecommerce.constants.endpoints.api.ApiUserEndpointRoutes;
import com.ecommerce.dto.UserDto;
import com.ecommerce.model.User;
import com.ecommerce.service.IUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ApiUserController {
    private final IUserService iUserService;

    public ApiUserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping(ApiUserEndpointRoutes.API_USERS)
    public List<UserDto> getAllUsers() {
        return iUserService.convertUserToDto(iUserService.findAll());
    }

    @GetMapping(ApiUserEndpointRoutes.API_USERS_PAGED)
    public Page<UserDto> getAllUsersPaged(Pageable pageable) {
        Page<User> userPage = iUserService.findAll(pageable);
        return userPage.map(iUserService::convertUserToDto);
    }

    @GetMapping(ApiUserEndpointRoutes.API_USER_BY_ID)
    public UserDto getUserById(@PathVariable Integer id) {
        return iUserService.convertUserToDto(iUserService.findById(id));
    }

    @PostMapping(ApiUserEndpointRoutes.API_USER_LOGIN)
    public UserDto login(@RequestParam String username, @RequestParam String password) {
        return iUserService.convertUserToDto(iUserService.login(username, password));
    }

    @PostMapping(ApiUserEndpointRoutes.API_USER_LOGOUT)
    public void logout() {
        iUserService.logout();
    }

    @PostMapping(ApiUserEndpointRoutes.API_USER_CREATE)
    public UserDto saveUser(@RequestBody User user) {
        return iUserService.convertUserToDto(iUserService.save(user));
    }

    @DeleteMapping(ApiUserEndpointRoutes.API_USER_DELETE_BY_ID)
    public void deleteUserById(@PathVariable Integer id) {
        iUserService.delete(id);
    }

    @GetMapping(ApiUserEndpointRoutes.API_USER_ID_FROM_TOKEN)
    public int getCurrentUserId() {
        return iUserService.getCurrentUserId();
    }

    @GetMapping(ApiUserEndpointRoutes.API_USER_IS_ADMIN)
    public boolean isAdmin() {
        return iUserService.isAdmin();
    }

    @GetMapping(ApiUserEndpointRoutes.API_USER_TOKEN_EXPIRE)
    public long getTokenExpirationDate() {
        return iUserService.getTokenExpirationDateInMinutes();
    }
}