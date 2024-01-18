package com.ecommerce.constants.endpoints.web;

public class HomeControllerWebEndpointRoutes {
    public static final String HOME = "/";
    public static final String PRODUCT_BY_ID = "/home/product/id/{id}";
    public static final String CART = "/home/cart";
    public static final String ADD_TO_CART = "/home/cart/add";
    public static final String DELETE_CART_PRODUCT = "/home/cart/delete/{productId}";
    public static final String PURCHASE_CONFIRM = "/home/purchase/confirm";
    public static final String ORDER_SUMMARY = "/home/order/summary";
    public static final String PRODUCT_SEARCH = "/home/search";
}
