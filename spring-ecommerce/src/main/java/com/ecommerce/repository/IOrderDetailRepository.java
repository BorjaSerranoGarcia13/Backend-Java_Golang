package com.ecommerce.repository;

import com.ecommerce.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderDetailRepository extends JpaRepository<OrderDetails,Integer> {
    List<OrderDetails> findByReference(String reference);

}
