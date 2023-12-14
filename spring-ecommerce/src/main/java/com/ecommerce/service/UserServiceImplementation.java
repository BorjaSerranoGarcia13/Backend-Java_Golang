package com.ecommerce.service;

import com.ecommerce.constants.messages.UserErrorMessages;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;
import com.ecommerce.repository.IUserRepository;
import com.ecommerce.utils.Validator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ecommerce.constants.messages.UserErrorMessages.*;
import static com.ecommerce.utils.StringUtils.*;

@Service
public class UserServiceImplementation implements IUserService {
    private final IUserRepository iUserRepository;
    BCryptPasswordEncoder passwordEncoder= new BCryptPasswordEncoder();

    public UserServiceImplementation(IUserRepository iUserRepository) {
        this.iUserRepository = iUserRepository;
    }

    @Override
    public User findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException(ERROR_USER_INVALID_USERNAME);
        }

        Optional<User> optionalUser = iUserRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new UserException(ERROR_USER_NOT_FOUND);
        }
        return optionalUser.get();
    }

    @Override
    public User findByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException(UserErrorMessages.ERROR_USER_INVALID_EMAIL);
        }

        return iUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ERROR_USER_NOT_FOUND));
    }

    @Override
    public List<User> findAll() {
        return iUserRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        if (id == null) {
            throw new UserException(ERROR_USER_INVALID_ID);
        }

        Optional<User> optionalUser = iUserRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserException(ERROR_USER_NOT_FOUND);
        }
        return optionalUser.get();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public User save(User user) {
        List<String> validationErrors = new ArrayList<>(Validator.validateRegisterUser(user));
        validationErrors.addAll(checkUserExists(user));
        if (!validationErrors.isEmpty()) {
            throw new UserException(String.join(", ", validationErrors));
        }

        user.setAdmin(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("3");

        return iUserRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        User user = findById(id);

        if (!user.getAdmin()) {
            throw new UserException(ERROR_ADMIN_CANNOT_DELETE);
        }

        iUserRepository.deleteById(id);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_EMAIL);
        }
        if (password == null || password.isEmpty()) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_PASSWORD);
        }

        username = removeWhitespaceAndConvertToLowerCase(username);

        User user = findByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(UserErrorMessages.ERROR_INVALID_CREDENTIALS);
        }

        return user;
    }

    public List<String> checkUserExists(User user) {
        List<String> errors = new ArrayList<>();

        List<User> foundUsers = iUserRepository.findByUsernameOrEmailOrPhoneNumber(user.getUsername(),
                user.getEmail(), user.getPhoneNumber());
        for (User foundUser : foundUsers) {
            if (foundUser.getUsername().equals(user.getUsername())) {
                errors.add(UserErrorMessages.ERROR_USER_USERNAME_ALREADY_IN_USE);
            }
            if (foundUser.getEmail().equals(user.getEmail())) {
                errors.add(UserErrorMessages.ERROR_USER_EMAIL_ALREADY_IN_USE);
            }
            if (foundUser.getPhoneNumber().equals(user.getPhoneNumber())) {
                errors.add(UserErrorMessages.ERROR_USER_PHONE_NUMBER_ALREADY_IN_USE);
            }
        }

        return errors;
    }

}
