package com.ecommerce.model;

import com.ecommerce.model.Product;
import com.ecommerce.constants.messages.ProductExceptionMessages;
import com.ecommerce.exception.ProductException;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProductTest {

    @Test
    public void testSetId() {
        Product product = new Product();
        product.setId(1);
        assertEquals(1, product.getId());

        assertThrows(ProductException.class, () -> product.setId(-1));
    }

    @Test
    public void testSetReference() {
        Product product = new Product();
        product.setReference("PROD_123");
        assertEquals("PROD_123", product.getReference());

        assertThrows(ProductException.class, () -> product.setReference("123"));
    }

    @Test
    public void testSetName() {
        Product product = new Product();
        product.setName("Test Product");
        assertEquals("Test Product", product.getName());

        assertThrows(ProductException.class, () -> product.setName(null));
    }

    @Test
    public void testSetDescription() {
        Product product = new Product();
        product.setDescription("Test Description");
        assertEquals("Test Description", product.getDescription());

        assertThrows(ProductException.class, () -> product.setDescription(null));
    }

    @Test
    public void testSetPrice() {
        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), product.getPrice());

        assertThrows(ProductException.class, () -> product.setPrice(BigDecimal.valueOf(-1)));
    }

    @Test
    public void testSetQuantity() {
        Product product = new Product();
        product.setQuantity(10);
        assertEquals(10, product.getQuantity());

        assertThrows(ProductException.class, () -> product.setQuantity(-1));
    }

    @Test
    public void testSetUserId() {
        Product product = new Product();
        product.setUserId(1);
        assertEquals(1, product.getUserId());

        assertThrows(ProductException.class, () -> product.setUserId(-1));
    }

    @Test
    public void testSetDeleted() {
        Product product = new Product();
        product.setDeleted(true);
        assertEquals(true, product.getDeleted());

        product.setDeleted(false);
        assertEquals(false, product.getDeleted());
    }
}
