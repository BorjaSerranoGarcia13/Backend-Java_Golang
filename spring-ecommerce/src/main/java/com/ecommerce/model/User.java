package com.ecommerce.model;

import com.ecommerce.config.SimpleGrantedAuthority;
import com.ecommerce.constants.messages.UserExceptionMessages;
import com.ecommerce.exception.UserException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Schema(description = "User entity")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the user", example = "1")
    private Integer id;

    @Schema(description = "Name of the user", example = "User name")
    private String name;

    @Column(unique = true)
    @Schema(description = "Username of the user", example = "username1")
    private String username;

    @Schema(description = "Password of the user", example = "123")
    private String password;

    @Column(unique = true)
    @Schema(description = "Email of the user", example = "username1@example.com")
    private String email;

    @Schema(description = "Address of the user", example = "123 Main St")
    private String address;

    @Column(unique = true)
    @Schema(description = "Phone number of the user", example = "1234567890")
    private String phoneNumber;

    private List<SimpleGrantedAuthority> authorities;

    public User() {
    }

    public User(Integer id, String name, String username, String password, String email, String address,
                String phoneNumber) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        if (id != null && id <= 0) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_ID);
        }
        this.id = id;
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

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_USERNAME);
        }
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_PASSWORD);
        }
        this.password = password;
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
        return authorities != null ? authorities : new ArrayList<>();
    }

    public void setAuthorities(List<SimpleGrantedAuthority> authorities) {
        if (authorities == null) {
            throw new UserException(UserExceptionMessages.ERROR_USER_INVALID_AUTHORITIES);
        }
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
