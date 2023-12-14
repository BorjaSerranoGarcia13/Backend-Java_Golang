package com.ecommerce.utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ReferenceGeneratorUtilsTest {

    @Test
    public void testGenerateUniqueReference_ValidPrefix() {
        String reference = ReferenceGeneratorUtils.generateUniqueReference(ReferenceGeneratorUtils.PRODUCT_PREFIX);
        assertTrue(reference.startsWith(ReferenceGeneratorUtils.PRODUCT_PREFIX));
        assertEquals(36, reference.length());
    }

    @Test
    public void testGenerateUniqueReference_InvalidPrefix() {
        assertThrows(IllegalArgumentException.class, () -> ReferenceGeneratorUtils.generateUniqueReference("INVALID_PREFIX"));
    }

    @Test
    public void testGenerateUniqueReference_NullPrefix() {
        assertThrows(IllegalArgumentException.class, () -> ReferenceGeneratorUtils.generateUniqueReference(null));
    }

    @Test
    public void testGenerateUniqueReference_EmptyPrefix() {
        assertThrows(IllegalArgumentException.class, () -> ReferenceGeneratorUtils.generateUniqueReference(""));
    }
}
