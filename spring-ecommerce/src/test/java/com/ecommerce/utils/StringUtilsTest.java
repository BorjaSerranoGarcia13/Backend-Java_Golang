package com.ecommerce.utils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringUtilsTest {

    @Test
    public void testRemoveExtraWhitespaces() {
        assertEquals("test string", StringUtils.removeExtraWhitespaces("test  string"));
        assertEquals("test string", StringUtils.removeExtraWhitespaces(" test string "));
        assertEquals("test string", StringUtils.removeExtraWhitespaces("test string  "));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.removeExtraWhitespaces(null));
    }

    @Test
    public void testRemoveNonAlphanumericAndConvertToLowerCase() {
        assertEquals("teststring", StringUtils.removeNonAlphanumericAndConvertToLowerCase("Test String!"));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.removeNonAlphanumericAndConvertToLowerCase(null));
    }

    @Test
    public void testRemoveWhitespaceAndConvertToLowerCase() {
        assertEquals("teststring", StringUtils.removeWhitespaceAndConvertToLowerCase(" Test String "));
        assertThrows(IllegalArgumentException.class, () -> StringUtils.removeWhitespaceAndConvertToLowerCase(null));
    }

    @Test
    public void testIsValidEmail() {
        assertTrue(StringUtils.isValidEmail("test@example.com"));
        assertFalse(StringUtils.isValidEmail("testexample.com"));
        assertFalse(StringUtils.isValidEmail(null));
    }

    @Test
    public void testIsValidPhoneNumber() {
        assertTrue(StringUtils.isValidPhoneNumber("1234567890"));
        assertFalse(StringUtils.isValidPhoneNumber("123-456-7890"));
        assertFalse(StringUtils.isValidPhoneNumber(null));
    }

    @Test
    public void testIsValidBigDecimal() {
        assertTrue(StringUtils.isValidBigDecimal("123.45"));
        assertFalse(StringUtils.isValidBigDecimal("123,45"));
        assertFalse(StringUtils.isValidBigDecimal(null));
    }

    @Test
    public void testIsValidInteger() {
        assertTrue(StringUtils.isValidInteger("12345"));
        assertFalse(StringUtils.isValidInteger("123.45"));
        assertFalse(StringUtils.isValidInteger(null));
    }
}
