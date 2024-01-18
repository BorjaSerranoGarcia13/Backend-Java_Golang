package com.ecommerce.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.constants.messages.IllegalArgumentExceptionMessages.ERROR_STRING_NULL;
import static com.ecommerce.utils.StringUtil.removeWhitespaceAndConvertToLowerCase;


@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final IUserService iUserService;

    public UserDetailsServiceImplementation(@Lazy IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException(ERROR_STRING_NULL);
        }

        return iUserService.findByUsername(removeWhitespaceAndConvertToLowerCase(username));
    }
}
