package com.ecommerce.constants.messages;

import com.ecommerce.utils.ReferenceGeneratorUtil;

public class ProductExceptionMessages {
    public static final String ERROR_PRODUCT_INVALID = "Product cannot be null";
    public static final String ERROR_PRODUCT_INVALID_USER_ID = "Product userID cannot be null or empty.";
    public static final String ERROR_PRODUCT_INVALID_ID = "Product id cannot be null, negative or 0.";
    public static final String ERROR_PRODUCT_INVALID_FIELD = "Product can only or must have name, description, price, " +
            "quantity fields.";
    public static final String ERROR_PRODUCT_INVALID_NAME = "Product name cannot be null or empty.";
    public static final String ERROR_PRODUCT_INVALID_DESCRIPTION = "Product description cannot be null or empty.";
    public static final String ERROR_PRODUCT_INVALID_IMAGE = "Product image cannot be null or empty..";
    public static final String ERROR_PRODUCT_INVALID_PRICE = "Product price cannot be null, negative or 0.";
    public static final String ERROR_PRODUCT_INVALID_QUANTITY = "Product quantity cannot be null or negative or " +
            "superior to 10.000.";
    public static final String ERROR_PRODUCT_INVALID_REFERENCE = "Product reference must start with "
            + ReferenceGeneratorUtil.PRODUCT_PREFIX + ".";
    public static final String ERROR_PRODUCT_INVALID_USER = "Product user cannot be null.";
    public static final String ERROR_PRODUCT_INVALID_ADMIN_USER = "Product user must be an admin user.";

    public static final String ERROR_PRODUCT_NOT_FOUND = "Product could not be found";
    public static final String ERROR_PRODUCT_ALREADY_EXISTS = "Product name or reference already exists";
    public static final String ERROR_PRODUCT_NOT_FOUND_OR_NAME_ALREADY_EXISTS = "Product could not be found or product name" +
            " already exists";
    public static final String ERROR_PRODUCT_INVALID_SEARCH_TERM = "Product invalid search term. It can not be empty.";
    public static final String ERROR_PRODUCT_INVALID_SEARCH_TYPE = "Product invalid search type. Must be 'name' or " +
            "'reference'";

    public static final String ERROR_PRODUCT_INVALID_NAME_MAX_LENGTH = "Product name max length exceeded. Limit is 20 characters";
    public static final String ERROR_PRODUCT_INVALID_DESCRIPTION_MAX_LENGTH = "Product description max length exceeded. Limit is 40 characters";
    public static final String ERROR_PRODUCT_INVALID_PRICE_MAX_ = "Product price max exceeded. Limit is '9999.99'.";
    public static final String ERROR_PRODUCT_INVALID_QUANTITY_MAX = "Product quantity max exceeded. Limit is '999'.";
}
