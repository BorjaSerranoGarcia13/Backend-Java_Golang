package com.ecommerce.service;

import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderDetailService {
    List<OrderDetails> findByReference(String reference);
    OrderDetails findById(Integer id);
    @Transactional
    void save(HttpSession session);
    @Transactional
    void delete(Integer id, HttpSession session);
    List<OrderDetails> getOrderDetailsList();
    void addProductToOrderDetailList(String reference, Integer quantity);
    void updateOrderDetailList(Product product, Integer quantity);
    void deleteProductInOrderDetailList(Integer id);
    BigDecimal getTotalPrice();

    Integer getCurrentOrderDetailsProductQuantity(String reference);
}
