package com.ecommerce.model;

import com.ecommerce.constants.messages.UserErrorMessages;
import com.ecommerce.exception.UserException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true)
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String address;
    @Column(unique = true)
    private String phoneNumber;
    private Boolean admin;

    @OneToMany(mappedBy = "user")
    private List<Product> products;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;

    public User() {
    }

    public User(Integer id, String name, String username, String password, String email, String address, String phoneNumber, Boolean admin) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.admin = admin;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        if (id != null && id <= 0) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_ID);
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        if (name == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_NAME);
        }
        this.name = name;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        if (username == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_USERNAME);
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        if (password == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_PASSWORD);
        }
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if (email == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_EMAIL);
        }
        this.email = email;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        if (address == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_ADDRESS);
        }
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_PHONE_NUMBER);
        }
        this.phoneNumber = phoneNumber;
    }

    public Boolean getAdmin() {
        return admin;
    }
    public void setAdmin(Boolean admin) {
        if (admin == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_ADMIN);
        }
        this.admin = admin;
    }

    public List<Product> getProducts() {
        return products != null ? products : new ArrayList<>();
    }
    public void setProducts(List<Product> products) {
        if (products == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_PRODUCTS);
        }
        this.products = products;
    }

    public List<Order> getOrders() {
        return orders != null ? orders : new ArrayList<>();
    }
    public void setOrders(List<Order> orders) {
        if (orders == null) {
            throw new UserException(UserErrorMessages.ERROR_USER_INVALID_ORDERS);
        }
        this.orders = orders;
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
                ", admin=" + admin +
                ", products=" + products +
                '}';
    }
}
