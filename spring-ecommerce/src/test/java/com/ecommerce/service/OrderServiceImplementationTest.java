package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.User;
import com.ecommerce.repository.IOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderServiceImplementationTest {

    @Mock
    private IOrderRepository iOrderRepository;

    private OrderServiceImplementation orderService;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        orderService = new OrderServiceImplementation(iOrderRepository);
    }

    @Test
    public void testSave() {
        OrderDetails orderDetails = new OrderDetails();
        User user = new User();
        List<OrderDetails> orderDetailsList = List.of(orderDetails);

        Order order = new Order(1, "DOrderRef", new Date(0L), new Date(0L), BigDecimal.valueOf(1.00), user, orderDetailsList);
        when(iOrderRepository.save(order)).thenReturn(order);

        Order result = orderService.save(order);

        assertEquals(order, result);
        verify(iOrderRepository, times(1)).save(order);
    }

    @Test
    public void testUpdate() {
        OrderDetails orderDetails = new OrderDetails();
        User user = new User();
        List<OrderDetails> orderDetailsList = List.of(orderDetails);

        Order order = new Order(1, "DOrderRef", new Date(0L), new Date(0L), BigDecimal.valueOf(1.00), user, orderDetailsList);

        orderService.update(order);

        verify(iOrderRepository, times(1)).save(order);
    }

    @Test
    public void testDelete() {
        orderService.delete(1);

        verify(iOrderRepository, times(1)).deleteById(1);
    }

    @Test
    public void testFindByReference() {
        Order order = new Order();
        when(iOrderRepository.findByReference("DOrderRef")).thenReturn(Optional.of(order));

        Order result = orderService.findByReference("DOrderRef");

        assertEquals(order, result);
        verify(iOrderRepository, times(1)).findByReference("DOrderRef");
    }

    @Test
    public void testFindById() {
        Order order = new Order();
        when(iOrderRepository.findById(1)).thenReturn(Optional.of(order));

        Order result = orderService.findById(1);

        assertEquals(order, result);
        verify(iOrderRepository, times(1)).findById(1);
    }

    @Test
    public void  testFindAll() {
        Order order1 = new Order();
        Order order2 = new Order();
        List<Order> orders = Arrays.asList(order1, order2);
        when(iOrderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.findAll();

        assertEquals(orders, result);
        verify(iOrderRepository, times(1)).findAll();
    }
}