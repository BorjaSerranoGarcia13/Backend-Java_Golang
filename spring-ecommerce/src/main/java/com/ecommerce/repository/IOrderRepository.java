package com.ecommerce.repository;

import com.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOrderRepository extends JpaRepository<Order, Integer> {
    Optional<Order> findByReference(String reference);

    List<Order> findAllByUserId(Integer id);
}
