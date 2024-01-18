package com.ecommerce.constants.messages;

import com.ecommerce.utils.ReferenceGeneratorUtil;

public class OrderExceptionMessages {
    public static final String ERROR_ORDER_INVALID = "Order cannot be null.";
    public static final String ERROR_ORDER_INVALID_USER_ID = "Order userID cannot be null or empty.";
    public static final String ERROR_ORDER_INVALID_TOTAL = "Order total cannot be null, negative or 0.";
    public static final String ERROR_ORDER_INVALID_REFERENCE = "Order reference cannot be null.";
    public static final String ERROR_ORDER_INVALID_REFERENCE_PREFIX = "Order reference must start with " + ReferenceGeneratorUtil.ORDER_PREFIX + ".";
    public static final String ERROR_ORDER_INVALID_ORDER_DETAILS = "Order orderDetails cannot be null.";
    public static final String ERROR_ORDER_INVALID_ID = "Order id cannot be null, negative or 0.";
    public static final String ERROR_ORDER_INVALID_CREATION_DATE = "Order creationDate cannot be null.";
    public static final String ERROR_ORDER_INVALID_RECEIVED_DATE = "Order receivedDate cannot be null.";
    public static final String ERROR_ORDER_NOT_FOUND = "Order could not be found in the database.";


}
