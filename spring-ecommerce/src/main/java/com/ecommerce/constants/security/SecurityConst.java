package com.ecommerce.constants.security;

import java.util.List;
import java.util.Arrays;

import com.ecommerce.constants.endpoints.api.ApiUserEndpointRoutes;
import com.ecommerce.constants.endpoints.web.UserWebEndpointRoutes;
import com.ecommerce.constants.endpoints.web.HomeControllerWebEndpointRoutes;

public class SecurityConst {

    public static final List<String> PERMIT_ALL_ENDPOINT_LIST = Arrays.asList(
            "/", "/home/**", "/vendor/**", "/css/**", "/images/**", "/swagger-ui/**",
            "/static/swagger-config.json",
            UserWebEndpointRoutes.LOGIN, UserWebEndpointRoutes.AUTHENTICATE,
            UserWebEndpointRoutes.LOGOUT, UserWebEndpointRoutes.CREATE, UserWebEndpointRoutes.SAVE,
            HomeControllerWebEndpointRoutes.HOME, HomeControllerWebEndpointRoutes.PRODUCT_BY_ID,
            HomeControllerWebEndpointRoutes.PRODUCT_SEARCH, ApiUserEndpointRoutes.API_USER_CREATE,
            ApiUserEndpointRoutes.API_USER_LOGIN, ApiUserEndpointRoutes.API_USER_LOGOUT,
            "/swagger-ui/**", "/api-docs/**"
    );

    public static final List<String> ADMIN_ENDPOINT_LIST = Arrays.asList(
            "/api/**", "/admin/**"
    );

    public static final List<String> AUTHENTICATED_ENDPOINT_LIST = Arrays.asList(
            HomeControllerWebEndpointRoutes.PURCHASE_CONFIRM,
            UserWebEndpointRoutes.PURCHASE_DETAILS, UserWebEndpointRoutes.PURCHASES,
            HomeControllerWebEndpointRoutes.ORDER_SUMMARY
    );
}
