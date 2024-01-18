package com.ecommerce.service;

import com.ecommerce.config.SimpleGrantedAuthority;
import com.ecommerce.constants.messages.UserExceptionMessages;
import com.ecommerce.dto.UserDto;
import com.ecommerce.exception.UserException;
import com.ecommerce.model.User;
import com.ecommerce.repository.IUserRepository;
import com.ecommerce.security.CustomAuthenticationToken;
import com.ecommerce.security.JwtUtil;
import com.ecommerce.security.SecurityContextUtil;
import com.ecommerce.utils.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.ecommerce.constants.messages.UserExceptionMessages.*;
import static com.ecommerce.utils.CookieUtil.createAndSetTokenCookie;
import static com.ecommerce.utils.CookieUtil.getTokenFromCookie;
import static com.ecommerce.utils.StringUtil.*;

@Service
public class UserServiceImplementation implements IUserService {
    private final IUserRepository iUserRepository;
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private UserDetailsServiceImplementation userDetailsService;
    private final ModelMapper modelMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public UserServiceImplementation(IUserRepository iUserRepository, ModelMapper modelMapper) {
        this.iUserRepository = iUserRepository;
        this.modelMapper = modelMapper;
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
            throw new IllegalArgumentException(UserExceptionMessages.ERROR_USER_INVALID_EMAIL);
        }

        return iUserRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(ERROR_USER_NOT_FOUND));
    }

    @Override
    public List<User> findAll() {
        return iUserRepository.findAll();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return iUserRepository.findAll(pageable);
    }

    @Override
    public User findById(Integer id) {
        if (id == null || id <= 0) {
            throw new UserException(ERROR_USER_NOT_LOGGED);
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
        if (user == null) {
            throw new UserException(ERROR_USER_INVALID);
        }
        if (user.getId() != null ||
                user.getName() == null || user.getName().isEmpty() ||
                user.getUsername() == null || user.getUsername().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty() ||
                user.getAddress() == null || user.getAddress().isEmpty()) {
            throw new UserException(ERROR_USER_INVALID_FIELD);
        }

        List<String> validationErrors = new ArrayList<>(validateUser(user));
        validationErrors.addAll(checkUserExists(user));
        if (!validationErrors.isEmpty()) {
            throw new UserException(String.join(", ", validationErrors));
        }

        user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        iUserRepository.save(user);

        authenticateUserAndCreateToken(user);

        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {
        if (id == null || id <= 0) {
            throw new UserException(ERROR_USER_INVALID_ID);
        }
        if (!jwtUtil.isAdminFromToken(getTokenFromCookie())) {
            throw new UserException(ERROR_NOT_ADMIN_USER_LOGGED_IN);
        }

        User user = findById(id);
        List<SimpleGrantedAuthority> authorities = user.getAuthorities();

        boolean isAdmin = authorities.stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));

        if (isAdmin) {
            throw new UserException(ERROR_ADMIN_USER_DELETION_NOT_ALLOWED);
        }

        iUserRepository.deleteById(id);
    }

    @Override
    public User login(String username, String password) {
        if (username == null || username.isEmpty()) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_USERNAME);
        }
        if (password == null || password.isEmpty()) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_PASSWORD);
        }

        User user = (User) userDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UserException(ERROR_USER_INVALID_PASSWORD);
        }

        authenticateUserAndCreateToken(user);

        return user;
    }

    @Override
    public void logout() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).
                getResponse();

        if (response == null) {
            throw new IllegalStateException("Response object is null");
        }
        CookieUtil.deleteTokenCookie(response);

        SecurityContextUtil.clearSecurityContext();
    }

    @Override
    public int getCurrentUserId() {
        return SecurityContextUtil.getUserId();
    }

    @Override
    public boolean isAdmin() {
        return SecurityContextUtil.isAdmin();
    }

    private void authenticateUserAndCreateToken(User user) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).
                getResponse();

        String token = jwtUtil.generateToken(user);
        createAndSetTokenCookie(token, response);

        CustomAuthenticationToken auth = new CustomAuthenticationToken(user.getUsername(), user.getId(),
                user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Override
    public long getTokenExpirationDateInMinutes() {
        Date expirationDate = jwtUtil.extractExpiration(getTokenFromCookie());
        long time = Math.abs(expirationDate.getTime() - new Date().getTime());
        return TimeUnit.MINUTES.convert(time, TimeUnit.MILLISECONDS);
    }

    @Override
    public UserDto convertUserToDto(User user) {
        UserDto userDto = new UserDto();
        modelMapper.map(user, userDto);

        return userDto;
    }

    @Override
    public List<UserDto> convertUserToDto(List<User> users) {
        return users.stream()
                .map(this::convertUserToDto)
                .collect(Collectors.toList());
    }

    public List<String> checkUserExists(User user) {
        List<String> errors = new ArrayList<>();

        List<User> foundUsers = iUserRepository.findByUsernameOrEmailOrPhoneNumber(user.getUsername(),
                user.getEmail(), user.getPhoneNumber());
        for (User foundUser : foundUsers) {
            if (foundUser.getUsername().equals(user.getUsername())) {
                errors.add(UserExceptionMessages.ERROR_USER_USERNAME_ALREADY_IN_USE);
            }
            if (foundUser.getEmail().equals(user.getEmail())) {
                errors.add(UserExceptionMessages.ERROR_USER_EMAIL_ALREADY_IN_USE);
            }
            if (foundUser.getPhoneNumber().equals(user.getPhoneNumber())) {
                errors.add(UserExceptionMessages.ERROR_USER_PHONE_NUMBER_ALREADY_IN_USE);
            }
        }

        return errors;
    }

    @Override
    public List<String> validateUser(User user) {
        List<String> errors = new ArrayList<>();

        if (user == null) {
            errors.add(ERROR_USER_INVALID);
            return errors;
        }

        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            errors.add(ERROR_USER_INVALID_USERNAME);
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.add(ERROR_USER_INVALID_PASSWORD);
        }

        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            errors.add(ERROR_USER_INVALID_EMAIL);
        }

        if (user.getPhoneNumber() == null || user.getPhoneNumber().isEmpty()) {
            errors.add(ERROR_USER_INVALID_PHONE_NUMBER);
        }

        if (user.getAddress() == null || user.getAddress().isEmpty()) {
            errors.add(ERROR_USER_INVALID_ADDRESS);
        }

        user.setName(removeNonAlphanumericAndConvertToLowerCase(user.getName()));
        user.setUsername(removeWhitespaceAndConvertToLowerCase(user.getUsername()));
        user.setEmail(removeWhitespaceAndConvertToLowerCase(user.getEmail()));

        if (!isValidEmail(user.getEmail())) {
            errors.add(ERROR_USER_INVALID_EMAIL);
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            errors.add(ERROR_USER_INVALID_PHONE_NUMBER);
        }

        return errors;
    }

    @Override
    public List<String> validateUsers(List<User> users) {
        List<String> errors = new ArrayList<>();

        if (users == null || users.isEmpty()) {
            errors.add(ERROR_USER_INVALID);
            return errors;
        }

        for (User user : users) {
            List<String> userErrors = validateUser(user);
            errors.addAll(userErrors);
        }

        return errors;
    }
}
