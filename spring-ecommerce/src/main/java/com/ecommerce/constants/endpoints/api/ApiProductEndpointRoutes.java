package com.ecommerce.constants.endpoints.api;

public class ApiProductEndpointRoutes {
    public static final String API_PRODUCTS = "/api/v1/products";
    public static final String API_PRODUCTS_PAGED = "/api/v1/products/paged";
    public static final String API_PRODUCT_BY_ID = "/api/v1/products/id/{id}";
    public static final String API_PRODUCT_BY_REFERENCE = "/api/v1/products/reference/{reference}";
    public static final String API_PRODUCTS_BY_REFERENCE = "/api/v1/products/references/{reference}";
    public static final String API_PRODUCTS_SEARCH_BY_NAME_OR_REFERENCE =
            "/api/v1/products/search";
    public static final String API_PRODUCT_CREATE = "/api/v1/products/create";
    public static final String API_PRODUCT_UPDATE = "/api/v1/products/update";
    public static final String API_PRODUCT_DELETE = "/api/v1/products/delete/{id}";
}
