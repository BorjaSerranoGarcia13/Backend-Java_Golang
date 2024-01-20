package com.ecommerce.controller.restcontroller;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.model.Order;
import com.ecommerce.service.IOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiOrderControllerTest {
    @Mock
    private IOrderService iOrderService;
    private ApiOrderController apiOrderController;

    @BeforeEach
    void setUp() {
        apiOrderController = new ApiOrderController(iOrderService);
    }

    @Test
    void getAllOrders() {
        List<OrderDto> expected = List.of(new OrderDto());
        when(iOrderService.findAll()).thenReturn(List.of(new Order()));
        when(iOrderService.convertOrderToDto(anyList())).thenReturn(expected);

        List<OrderDto> actual = apiOrderController.getAllOrders();

        assertEquals(expected, actual);
    }

    @Test
    void getAllOrdersPaged() {
        Page<Order> orderPage = new PageImpl<>(List.of(new Order()));
        Page<OrderDto> expected = orderPage.map(iOrderService::convertOrderToDto);

        when(iOrderService.findAll(any(Pageable.class))).thenReturn(orderPage);

        Page<OrderDto> actual = apiOrderController.getAllOrdersPaged(Pageable.unpaged());

        assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    void getOrderById() {
        OrderDto expected = new OrderDto();
        when(iOrderService.findById(anyInt())).thenReturn(new Order());
        when(iOrderService.convertOrderToDto(any(Order.class))).thenReturn(expected);

        OrderDto actual = apiOrderController.getOrderById(1);

        assertEquals(expected, actual);
    }

    @Test
    void getOrderByReference() {
        OrderDto expected = new OrderDto();
        when(iOrderService.findByReference(anyString())).thenReturn(new Order());
        when(iOrderService.convertOrderToDto(any(Order.class))).thenReturn(expected);

        OrderDto actual = apiOrderController.getOrderByReference("reference");

        assertEquals(expected, actual);
    }

    @Test
    void getOrdersByUserId() {
        List<OrderDto> expected = List.of(new OrderDto());
        when(iOrderService.findByUserId(anyInt())).thenReturn(List.of(new Order()));
        when(iOrderService.convertOrderToDto(anyList())).thenReturn(expected);

        List<OrderDto> actual = apiOrderController.getOrdersByUserId(1);

        assertEquals(expected, actual);
    }

    @Test
    void deleteOrderById() {
        int id = 1;

        apiOrderController.deleteOrderById(id);

        verify(iOrderService, times(1)).delete(id);
    }
}
