package com.ecommerce.constants.messages;

public class UserExceptionMessages {
    public static final String ERROR_USER_NOT_FOUND = "The user does not exist";
    public static final String ERROR_USER_NOT_LOGGED = "The user is not logged in";
    public static final String ERROR_USER_INVALID = "User cannot be null";
    public static final String ERROR_USER_INVALID_FIELD = "User can only or must have 'name', 'username', 'password', " +
            "'email', 'address', 'phoneNumber' fields.";
    public static final String ERROR_USER_INVALID_ID = "User id cannot be null, negative or 0.";
    public static final String ERROR_USER_INVALID_NAME = "User name cannot be null or empty.";
    public static final String ERROR_USER_INVALID_USERNAME = "User username cannot be null or empty.";
    public static final String ERROR_USER_INVALID_PHONE_NUMBER = "User phone number cannot be null or empty. Format must be '123'.";
    public static final String ERROR_USER_INVALID_EMAIL = "User email cannot be null or empty. Format must be 'example@example.com'.";
    public static final String ERROR_USER_INVALID_PASSWORD = "User password cannot be null or empty.";
    public static final String ERROR_USER_INVALID_ADDRESS = "User address cannot be null or empty.";
    public static final String ERROR_NOT_ADMIN_USER_LOGGED_IN = "The user is not logged or is not an administrator";
    public static final String ERROR_ADMIN_USER_DELETION_NOT_ALLOWED = "Deletion_of_an_administrator_user_is_not_permitted";
    public static final String ERROR_USER_USERNAME_ALREADY_IN_USE = "The username is already in use";
    public static final String ERROR_USER_EMAIL_ALREADY_IN_USE = "The email is already in use";
    public static final String ERROR_USER_PHONE_NUMBER_ALREADY_IN_USE = "The phone number is already in use";
    public static final String ERROR_USER_INVALID_AUTHORITIES = "The user authorities cannot be null or empty";

}
