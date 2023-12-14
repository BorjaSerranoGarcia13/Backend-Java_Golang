package com.ecommerce.utils;

import com.ecommerce.constants.messages.ProductErrorMessages;
import com.ecommerce.constants.messages.UserErrorMessages;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.ecommerce.constants.messages.OrderDetailsErrorMessages.*;
import static com.ecommerce.constants.messages.OrderErrorMessages.*;
import static com.ecommerce.constants.messages.ProductErrorMessages.*;
import static com.ecommerce.constants.messages.UserErrorMessages.*;
import static com.ecommerce.utils.StringUtils.*;
import static com.ecommerce.utils.StringUtils.isValidInteger;

public class Validator {
    public static List<String> validateOrder(Order order) {
        List<String> errors = new ArrayList<>();

        if (order == null) {
            errors.add(ERROR_ORDER_INVALID);
            return errors;
        }
        if (order.getReference() == null || order.getReference().isEmpty()) {
            errors.add(ERROR_ORDER_INVALID_REFERENCE);
        }

        if (order.getCreationDate() == null) {
            errors.add(ERROR_ORDER_INVALID_CREATION_DATE);
        }

        if (order.getTotal() == null || order.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(ERROR_ORDER_INVALID_TOTAL);
        }

        if (order.getOrderDetails() == null || order.getOrderDetails().isEmpty()) {
            errors.add(ERROR_ORDER_INVALID_ORDER_DETAILS);
        }

        if (order.getUser() == null) {
            errors.add(ERROR_ORDER_INVALID_USER);
        }

        return errors;
    }

    public static List<String> validateOrderDetails(OrderDetails orderDetails) {
        List<String> errors = new ArrayList<>();

        if (orderDetails == null) {
            errors.add(ERROR_ORDER_DETAILS_INVALID);
            return errors;
        }
        if (orderDetails.getProduct() == null ) {
            errors.add(ERROR_ORDER_DETAILS_INVALID_PRODUCT);
        }

        if (orderDetails.getPrice() == null || orderDetails.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(ERROR_ORDER_DETAILS_INVALID_PRICE);
        }

        if (orderDetails.getQuantity() == null || orderDetails.getQuantity() < 0) {
            errors.add(ERROR_ORDER_DETAILS_INVALID_QUANTITY);
        }

        if (orderDetails.getTotal() == null || orderDetails.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(ERROR_ORDER_DETAILS_INVALID_TOTAL);
        }

        return errors;
    }

    public static List<String> validateProduct(Product product) {
        List<String> errors = new ArrayList<>();

        if (product == null) {
            errors.add(ERROR_PRODUCT_INVALID);
            return errors;
        }

        if (product.getName() == null || product.getName().isEmpty()) {
            errors.add(ProductErrorMessages.ERROR_PRODUCT_INVALID_NAME);
        } else if (product.getName().length() > MAX_PRODUCT_NAME_LENGTH) {
            errors.add(ERROR_PRODUCT_INVALID_NAME_MAX_LENGTH);
        }

        if (product.getDescription() == null || product.getDescription().isEmpty()) {
            errors.add(ProductErrorMessages.ERROR_PRODUCT_INVALID_DESCRIPTION);
        } else if (product.getDescription().length() > MAX_PRODUCT_DESCRIPTION_LENGTH) {
            errors.add(ERROR_PRODUCT_INVALID_DESCRIPTION_MAX_LENGTH);
        }

        if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.add(ProductErrorMessages.ERROR_PRODUCT_INVALID_PRICE);
        } else if (product.getPrice().compareTo(MAX_PRODUCT_PRICE) > 0) {
            errors.add(ERROR_PRODUCT_INVALID_PRICE_MAX_);
        }

        if (product.getQuantity() < 0) {
            errors.add(ProductErrorMessages.ERROR_PRODUCT_INVALID_QUANTITY);
        } else if (product.getQuantity() > MAX_PRODUCT_QUANTITY) {
            errors.add(ERROR_PRODUCT_INVALID_QUANTITY_MAX);
        }

        product.setName(removeExtraWhitespaces(product.getName()));
        product.setDescription(removeExtraWhitespaces(product.getDescription()));

        if (!isValidBigDecimal(product.getPrice().toString())) {
            errors.add(ERROR_PRODUCT_INVALID_PRICE);
        }

        if (!isValidInteger(product.getQuantity().toString())) {
            errors.add(ERROR_PRODUCT_INVALID_QUANTITY);
        }

        return errors;
    }

    public static List<String> validateRegisterUser(User user) {
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
        user.setAddress(removeWhitespaceAndConvertToLowerCase(user.getAddress()));

        if (!isValidEmail(user.getEmail())) {
            errors.add(ERROR_USER_INVALID_EMAIL);
        }

        if (!isValidPhoneNumber(user.getPhoneNumber())) {
            errors.add(ERROR_USER_INVALID_PHONE_NUMBER);
        }

        return errors;
    }

}
