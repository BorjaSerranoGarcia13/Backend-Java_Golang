package com.ecommerce.model;

import com.ecommerce.exception.OrderDetailsException;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderDetailsTest {

    @Test
    public void testSetId() {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setId(1);
        assertEquals(1, orderDetails.getId());

        assertThrows(OrderDetailsException.class, () -> orderDetails.setId(-1));
    }

    @Test
    public void testSetQuantity() {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setQuantity(10);
        assertEquals(10, orderDetails.getQuantity());

        assertThrows(OrderDetailsException.class, () -> orderDetails.setQuantity(-1));
    }

    @Test
    public void testSetTotal() {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setTotal(BigDecimal.valueOf(100.00));
        assertEquals(BigDecimal.valueOf(100.00), orderDetails.getTotal());

        assertThrows(OrderDetailsException.class, () -> orderDetails.setTotal(BigDecimal.valueOf(-1)));
    }

    @Test
    public void testSetProduct() {
        OrderDetails orderDetails = new OrderDetails();
        Product product = new Product();
        orderDetails.setProduct(product);
        assertEquals(product, orderDetails.getProduct());

        assertThrows(OrderDetailsException.class, () -> orderDetails.setProduct(null));
    }
}
