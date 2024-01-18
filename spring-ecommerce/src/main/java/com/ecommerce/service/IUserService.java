package com.ecommerce.service;

import com.ecommerce.dto.UserDto;
import com.ecommerce.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    Page<User> findAll(Pageable pageable);

    User findById(Integer id);

    User findByUsername(String username);

    User findByEmail(String email);

    User login(String username, String password);

    void logout();

    @Transactional
    User save(User user);

    @Transactional
    void delete(Integer id);

    int getCurrentUserId();

    boolean isAdmin();

    UserDto convertUserToDto(User user);

    List<UserDto> convertUserToDto(List<User> users);

    List<String> validateUser(User user);

    List<String> validateUsers(List<User> users);

    long getTokenExpirationDateInMinutes();

}
