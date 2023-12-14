package com.ecommerce.service;

import com.ecommerce.model.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IUserService {
    User findByUsername(String username);

    User findByEmail(String email);

    @Transactional
    User save(User user);

    @Transactional
    void delete(Integer id);

    User login(String username, String password);

    User findById(Integer id);

    List<User> findAll();

}
