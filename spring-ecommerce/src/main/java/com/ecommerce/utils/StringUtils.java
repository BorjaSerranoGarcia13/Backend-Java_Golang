package com.ecommerce.utils;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import static com.ecommerce.constants.messages.IllegalArgumentErrorMessage.ERROR_STRING_NULL;

public class StringUtils {
    public static final Integer MAX_PRODUCT_NAME_LENGTH = 20;
    public static final Integer MAX_PRODUCT_DESCRIPTION_LENGTH = 80;
    public static final BigDecimal MAX_PRODUCT_PRICE = BigDecimal.valueOf(9999.99);
    public static final Integer MAX_PRODUCT_QUANTITY = 999;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$");
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^[0-9]*$");
    private static final Pattern BIG_DECIMAL_PATTERN = Pattern.compile("^[0-9.]*$");
    private static final Pattern INTEGER_PATTERN = Pattern.compile("^[0-9]*$");

    public static String removeExtraWhitespaces(String str) {
        if (str == null) {
            throw new IllegalArgumentException(ERROR_STRING_NULL);
        }
        return str.trim().replaceAll("\\s{2,}", " ");
    }

    public static String removeNonAlphanumericAndConvertToLowerCase(String name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_STRING_NULL);
        }
        return name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
    }

    public static String removeWhitespaceAndConvertToLowerCase(String name) {
        if (name == null) {
            throw new IllegalArgumentException(ERROR_STRING_NULL);
        }
        return name.replaceAll("\\s", "").toLowerCase();
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        return PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches();
    }

    public static boolean isValidBigDecimal(String bigDecimal) {
        if (bigDecimal == null) {
            return false;
        }
        return BIG_DECIMAL_PATTERN.matcher(bigDecimal).matches();
    }

    public static boolean isValidInteger(String integer) {
        return integer != null && integer.matches("^-?\\d+$");
    }
}
