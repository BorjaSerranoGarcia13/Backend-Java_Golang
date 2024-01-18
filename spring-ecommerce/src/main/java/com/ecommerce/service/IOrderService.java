package com.ecommerce.service;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService {
    List<Order> findAll();

    Page<Order> findAll(Pageable pageable);

    Order findByReference(String reference);

    Order findById(Integer id);

    List<Order> findByUserId(Integer userId);

    @Transactional
    Order save(List<OrderDetails> ordersDetails, BigDecimal totalPrice, Integer userId);

    @Transactional
    void delete(Integer id);

    OrderDto convertOrderToDto(Order order);

    List<OrderDto> convertOrderToDto(List<Order> orders);

    List<String> validateOrder(Order order);

    List<String> validateOrders(List<Order> orders);

}
