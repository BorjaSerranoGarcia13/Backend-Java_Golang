package com.ecommerce.service;

import com.ecommerce.model.Order;
import jakarta.transaction.Transactional;

import java.util.List;

public interface IOrderService {
    Order findByReference(String reference);
    Order findById(Integer id);
    @Transactional
    Order save(Order order);
    @Transactional
    void update(Order order);
    @Transactional
    void delete(Integer id);
    List<Order> findAll();
}
