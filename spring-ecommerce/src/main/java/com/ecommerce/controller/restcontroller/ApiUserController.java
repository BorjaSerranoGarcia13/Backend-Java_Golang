package com.ecommerce.controller.restcontroller;

import com.ecommerce.constants.endpoints.api.ApiUserEndpointRoutes;
import com.ecommerce.dto.UserDto;
import com.ecommerce.model.User;
import com.ecommerce.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "User API", description = "API for managing users")
public class ApiUserController {
    private final IUserService iUserService;

    public ApiUserController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @GetMapping(ApiUserEndpointRoutes.API_USERS)
    @Operation(summary = "Get all users", description = "Fetch all users from the database")
    public List<UserDto> getAllUsers() {
        return iUserService.convertUserToDto(iUserService.findAll());
    }

    @GetMapping(ApiUserEndpointRoutes.API_USERS_PAGED)
    @Operation(summary = "Get all users paged", description = "Fetch all users from the database with pagination")
    public Page<UserDto> getAllUsersPaged(Pageable pageable) {
        Page<User> userPage = iUserService.findAll(pageable);
        return userPage.map(iUserService::convertUserToDto);
    }

    @GetMapping(ApiUserEndpointRoutes.API_USER_BY_ID)
    @Operation(summary = "Get user by ID", description = "Fetch a user from the database by its ID")
    public UserDto getUserById(@PathVariable Integer id) {
        return iUserService.convertUserToDto(iUserService.findById(id));
    }

    @PostMapping(ApiUserEndpointRoutes.API_USER_LOGIN)
    @Operation(summary = "User login", description = "Login a user with username and password")
    public UserDto login(@RequestParam String username, @RequestParam String password) {
        return iUserService.convertUserToDto(iUserService.login(username, password));
    }

    @PostMapping(ApiUserEndpointRoutes.API_USER_LOGOUT)
    @Operation(summary = "User logout", description = "Logout the current user")
    public void logout() {
        iUserService.logout();
    }

    @PostMapping(ApiUserEndpointRoutes.API_USER_CREATE)
    @Operation(summary = "Create a user", description = "Create a new user in the database")
    public UserDto saveUser(@RequestBody User user) {
        return iUserService.convertUserToDto(iUserService.save(user));
    }

    @DeleteMapping(ApiUserEndpointRoutes.API_USER_DELETE_BY_ID)
    @Operation(summary = "Delete a user", description = "Delete a user from the database by its ID")
    public void deleteUserById(@PathVariable Integer id) {
        iUserService.delete(id);
    }

    @GetMapping(ApiUserEndpointRoutes.API_USER_ID_FROM_TOKEN)
    @Operation(summary = "Get current user ID from JWT", description = "Fetch the ID of the current user from JWT")
    public int getCurrentUserId() {
        return iUserService.getCurrentUserId();
    }

    @GetMapping(ApiUserEndpointRoutes.API_USER_IS_ADMIN)
    @Operation(summary = "Check if user is admin from JWT", description = "Check if the current user is an admin from JWT")
    public boolean isAdmin() {
        return iUserService.isAdmin();
    }

    @GetMapping(ApiUserEndpointRoutes.API_USER_TOKEN_EXPIRE)
    @Operation(summary = "Get token expiration date", description = "Fetch the expiration date of the current token")
    public String getTokenExpirationDate() {
        return "Token expiration date: " + iUserService.getTokenExpirationDateInMinutes() + " minutes";
    }
}