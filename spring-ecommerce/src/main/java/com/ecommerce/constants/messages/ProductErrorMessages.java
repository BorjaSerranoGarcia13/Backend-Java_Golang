package com.ecommerce.constants.messages;

import com.ecommerce.utils.ReferenceGeneratorUtils;

public class ProductErrorMessages {
    public static final String ERROR_PRODUCT_INVALID = "Product cannot be null";
    public static final String ERROR_PRODUCT_INVALID_ID = "Product id cannot be null, negative or 0.";
    public static final String ERROR_PRODUCT_INVALID_NAME = "Product name cannot be null or empty.";
    public static final String ERROR_PRODUCT_INVALID_DESCRIPTION = "Product description cannot be null or empty.";
    public static final String ERROR_PRODUCT_INVALID_IMAGE = "Product image cannot be null or empty..";
    public static final String ERROR_PRODUCT_INVALID_PRICE = "Product price cannot be null, negative or 0.";
    public static final String ERROR_PRODUCT_INVALID_QUANTITY = "Product quantity cannot be null or negative.";
    public static final String ERROR_PRODUCT_INVALID_REFERENCE = "Product reference must start with " + ReferenceGeneratorUtils.PRODUCT_PREFIX + ".";
    public static final String ERROR_PRODUCT_INVALID_USER = "Product user cannot be null.";
    public static final String ERROR_PRODUCT_INVALID_ADMIN_USER = "Product user must be an admin user.";

    public static final String ERROR_PRODUCT_NOT_FOUND = "Product could not be found";
    public static final String ERROR_PRODUCT_ALREADY_EXISTS = "Product already exists";

    public static final String ERROR_PRODUCT_INVALID_NAME_MAX_LENGTH = "Product name max length exceeded. Limit is 20 characters";
    public static final String ERROR_PRODUCT_INVALID_DESCRIPTION_MAX_LENGTH = "Product description max length exceeded. Limit is 40 characters";
    public static final String ERROR_PRODUCT_INVALID_PRICE_MAX_ = "Product price max exceeded. Limit is '9999.99'.";
    public static final String ERROR_PRODUCT_INVALID_QUANTITY_MAX = "Product quantity max exceeded. Limit is '999'.";
}
