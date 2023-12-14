package com.ecommerce.constants.messages;

public class OrderDetailsErrorMessages {
    public static final String ERROR_ORDER_DETAILS_INVALID = "OrderDetails cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_LIST = "OrderDetails list cannot be null or empty.";
    public static final String ERROR_ORDER_DETAILS_INVALID_TOTAL = "OrderDetails total cannot be null, negative or 0.";
    public static final String ERROR_ORDER_DETAILS_INVALID_REFERENCE = "OrderDetails reference cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_PRODUCT_REFERENCE = "OrderDetails product reference cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_PRICE = "OrderDetails price cannot be null, negative or 0.";
    public static final String ERROR_ORDER_DETAILS_INVALID_QUANTITY = "OrderDetails quantity cannot be null or negative.";
    public static final String ERROR_ORDER_DETAILS_INVALID_USER = "OrderDetails user cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_ORDER = "OrderDetails order cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_PRODUCT = "OrderDetails product cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_ID = "OrderDetails id cannot be null, negative or 0.";
    public static final String ERROR_ORDER_DETAILS_NOT_FOUND = "OrderDetails could not be found in the database.";
    public static final String ERROR_ORDER_DETAILS_INVALID_ADMIN = "OrderDetails user must be an admin.";

    public static final String ERROR_ORDER_DETAILS_INVALID_STOCK = "OrderDetails quantity cannot exceed the current stock of product quantity.";
}
