package com.ecommerce.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.ecommerce.constants.SessionAttributes.USER_ID;
import static com.ecommerce.constants.messages.IllegalArgumentErrorMessage.ERROR_STRING_NULL;
import static com.ecommerce.utils.StringUtils.removeWhitespaceAndConvertToLowerCase;


@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final HttpSession session;
    private final IUserService iUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDetailsServiceImplementation(@Lazy IUserService iUserService, HttpSession session,
                                            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.iUserService = iUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.session = session;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException(ERROR_STRING_NULL);
        }

        com.ecommerce.model.User user = iUserService.findByUsername(removeWhitespaceAndConvertToLowerCase(username));
        session.setAttribute(USER_ID, user.getId());

        return User.builder()
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .roles(user.getAdmin() ? "ADMIN" : "USER")
                .build();

    }
}
