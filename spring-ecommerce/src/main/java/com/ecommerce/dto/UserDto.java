package com.ecommerce.dto;

import com.ecommerce.config.SimpleGrantedAuthority;
import com.ecommerce.constants.messages.UserExceptionMessages;
import com.ecommerce.exception.UserException;

import java.util.List;

public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private String name;
    private String address;
    private String phoneNumber;
    private List<SimpleGrantedAuthority> authorities;

    public UserDto() {
    }

    public UserDto(Integer id, String username, String email, String name, String address, String phoneNumber,
                   List<SimpleGrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id == null || id < 0) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_ID);
        }
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_USERNAME);
        }
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_EMAIL);
        }
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_NAME);
        }
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_ADDRESS);
        }
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_PHONE_NUMBER);
        }
        this.phoneNumber = phoneNumber;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        if (authorities == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_AUTHORITIES);
        }
        this.authorities = authorities;
    }
}
