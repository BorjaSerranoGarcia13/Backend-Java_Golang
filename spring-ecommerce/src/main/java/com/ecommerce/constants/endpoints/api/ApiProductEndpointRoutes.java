package com.ecommerce.constants.endpoints.api;

public class ApiProductEndpointRoutes {
    public static final String API_PRODUCTS = "/api/products";
    public static final String API_PRODUCTS_PAGED = "/api/products/paged";
    public static final String API_PRODUCT_BY_ID = "/api/products/id/{id}";
    public static final String API_PRODUCT_BY_REFERENCE = "/api/products/reference/{reference}";
    public static final String API_PRODUCTS_BY_REFERENCE = "/api/products/references/{reference}";
    public static final String API_PRODUCTS_SEARCH_BY_NAME_OR_REFERENCE =
            "/api/products/search";
    public static final String API_PRODUCT_CREATE = "/api/products/create";
    public static final String API_PRODUCT_UPDATE = "/api/products/update";
    public static final String API_PRODUCT_DELETE = "/api/products/delete/{id}";
}
