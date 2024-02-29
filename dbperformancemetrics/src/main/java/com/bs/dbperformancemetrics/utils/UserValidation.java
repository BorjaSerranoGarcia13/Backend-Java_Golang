package com.bs.dbperformancemetrics.utils;

import com.bs.dbperformancemetrics.model.IUser;

import java.util.List;

public class UserValidation {

    public static <T extends IUser<?>> void validateUser(T user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getId() == null || user.getId().toString().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

    public static <T extends IUser<?>> void validateUsers(List<T> users) {
        for (T user : users) {
            validateUser(user);
        }
    }

    public static <T extends IUser<?>> void validateUserCreation(T user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

    public static <T extends IUser<?>> void validateUsersCreation(List<T> users) {
        for (T user : users) {
            validateUserCreation(user);
        }
    }

    public static <T extends IUser<?>> void validateUsersCreationParallel(List<T> users) {
        users.parallelStream().forEach(UserValidation::validateUserCreation);
    }
}