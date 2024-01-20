package com.ecommerce.model;

import com.ecommerce.exception.OrderException;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderTest {

    @Test
    public void testSetId() {
        Order order = new Order();
        order.setId(1);
        assertEquals(1, order.getId());

        assertThrows(OrderException.class, () -> order.setId(-1));
    }

    @Test
    public void testSetUserId() {
        Order order = new Order();
        order.setUserId(1);
        assertEquals(1, order.getUserId());

        assertThrows(OrderException.class, () -> order.setUserId(-1));
    }

    @Test
    public void testSetReference() {
        Order order = new Order();
        order.setReference("D_123");
        assertEquals("D_123", order.getReference());

        assertThrows(OrderException.class, () -> order.setReference("123"));
    }

    @Test
    public void testSetCreationDate() {
        Order order = new Order();
        Date date = new Date();
        order.setCreationDate(date);
        assertEquals(date, order.getCreationDate());

        assertThrows(OrderException.class, () -> order.setCreationDate(null));
    }

    @Test
    public void testSetReceivedDate() {
        Order order = new Order();
        Date date = new Date();
        order.setReceivedDate(date);
        assertEquals(date, order.getReceivedDate());

        assertThrows(OrderException.class, () -> order.setReceivedDate(null));
    }

    @Test
    public void testSetTotal() {
        Order order = new Order();
        order.setTotal(BigDecimal.valueOf(100.00));
        assertEquals(BigDecimal.valueOf(100.00), order.getTotal());

        assertThrows(OrderException.class, () -> order.setTotal(BigDecimal.valueOf(-1)));
    }

    @Test
    public void testSetOrderDetails() {
        Order order = new Order();
        List<OrderDetails> orderDetails = new ArrayList<>();
        order.setOrderDetails(orderDetails);
        assertEquals(orderDetails, order.getOrderDetails());

        assertThrows(OrderException.class, () -> order.setOrderDetails(null));
    }
}
