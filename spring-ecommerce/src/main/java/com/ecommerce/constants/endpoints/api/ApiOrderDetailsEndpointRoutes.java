package com.ecommerce.constants.endpoints.api;

public class ApiOrderDetailsEndpointRoutes {
    public static final String API_ORDER_DETAILS = "/api/v1/orders-details";
    public static final String API_ORDER_DETAILS_PAGED = "/api/v1/orders-details/paged";
    public static final String API_ORDER_DETAIL_BY_ID = "/api/v1/orders-details/id/{id}";
    public static final String API_ORDER_DETAILS_PRODUCT_STOCK = "/api/v1/product/stock/{productId}";
    public static final String API_ORDER_DETAILS_CART = "/api/v1/cart";
    public static final String API_ORDER_DETAILS_ADD_TO_CART = "/api/v1/cart/add";
    public static final String API_ORDER_DETAILS_REMOVE_FROM_CART = "/api/v1/cart/remove/{productId}";
    public static final String API_ORDER_DETAILS_CONFIRM_PURCHASE = "/api/v1/purchase/confirm";
}
