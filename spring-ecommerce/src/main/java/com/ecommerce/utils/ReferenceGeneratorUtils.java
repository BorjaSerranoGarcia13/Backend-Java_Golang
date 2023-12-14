package com.ecommerce.utils;

import java.util.Set;
import java.util.UUID;

import static com.ecommerce.constants.messages.IllegalArgumentErrorMessage.ERROR_PREFIX_NULL;

public class ReferenceGeneratorUtils {
    public static final String PRODUCT_PREFIX = "P";
    public static final String ORDER_PREFIX = "O";
    public static final String ORDER_DETAIL_PREFIX = "D";
    private static final Set<String> VALID_PREFIXES = Set.of(PRODUCT_PREFIX, ORDER_PREFIX, ORDER_DETAIL_PREFIX);

    public static String generateUniqueReference(String prefix) {
        if (prefix == null || prefix.isEmpty() || !VALID_PREFIXES.contains(prefix)) {
            throw new IllegalArgumentException(ERROR_PREFIX_NULL);
        }

        String randomPart = UUID.randomUUID().toString();

        return prefix.charAt(0) + randomPart.substring(1);
    }

}
