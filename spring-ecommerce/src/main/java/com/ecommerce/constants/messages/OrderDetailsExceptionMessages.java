package com.ecommerce.constants.messages;

import com.ecommerce.utils.ReferenceGeneratorUtil;

public class OrderDetailsExceptionMessages {
    public static final String ERROR_ORDER_DETAILS_INVALID = "OrderDetails cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_PRODUCT_ID = "OrderDetails productID cannot be null "
            + "or empty.";
    public static final String ERROR_ORDER_DETAILS_INVALID_PRODUCT_NAME = "OrderDetails productName cannot be null "
            + "or empty.";
    public static final String ERROR_ORDER_DETAILS_INVALID_ORDER_ID = "OrderDetails orderID cannot be null or empty.";

    public static final String ERROR_ORDER_DETAILS_EMPTY_CART = "OrderDetails cart cannot be null or empty.";
    public static final String ERROR_ORDER_DETAILS_INVALID_TOTAL = "OrderDetails total cannot be null, negative or 0.";
    public static final String ERROR_ORDER_DETAILS_INVALID_REFERENCE = "OrderDetails reference cannot be null. " +
            "It must start with '" + ReferenceGeneratorUtil.ORDER_PREFIX + "'.";
    public static final String ERROR_ORDER_DETAILS_INVALID_PRICE = "OrderDetails price cannot be null, negative or 0.";
    public static final String ERROR_ORDER_DETAILS_INVALID_QUANTITY = "OrderDetails quantity cannot be null or " +
            "negative.";
    public static final String ERROR_ORDER_DETAILS_INVALID_ORDER = "OrderDetails order cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_PRODUCT = "OrderDetails product cannot be null.";
    public static final String ERROR_ORDER_DETAILS_INVALID_ID = "OrderDetails id cannot be null, negative or 0.";
    public static final String ERROR_ORDER_DETAILS_NOT_FOUND = "OrderDetails could not be found in the database.";
    public static final String ERROR_ORDER_DETAILS_REMOVING_PRODUCT = "OrderDetails product could not be removed from "
            + "the cart.";
    public static final String ERROR_ORDER_DETAILS_INVALID_STOCK = "OrderDetails quantity cannot exceed the current "
            + "stock of product quantity.";

}
