package com.ecommerce.constants.endpoints.api;

public class ApiOrderDetailsEndpointRoutes {
    public static final String API_ORDER_DETAILS = "/api/orders-details";
    public static final String API_ORDER_DETAILS_PAGED = "/api/orders-details/paged";
    public static final String API_ORDER_DETAIL_BY_ID = "/api/orders-details/id/{id}";
    public static final String API_ORDER_DETAILS_PRODUCT_STOCK = "/api/product/stock/{productId}";
    public static final String API_ORDER_DETAILS_CART = "/api/cart";
    public static final String API_ORDER_DETAILS_ADD_TO_CART = "/api/cart/add";
    public static final String API_ORDER_DETAILS_REMOVE_FROM_CART = "/api/cart/remove/{productId}";
    public static final String API_ORDER_DETAILS_CONFIRM_PURCHASE = "/api/purchase/confirm";
}
