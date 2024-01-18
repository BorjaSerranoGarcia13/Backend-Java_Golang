package com.ecommerce.service;

import com.ecommerce.dto.OrderDetailsDto;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface IOrderDetailsService {
    List<OrderDetails> findAll();

    Page<OrderDetails> findAll(Pageable pageable);

    OrderDetails findById(Integer id);

    @Transactional
    Order save();

    @Transactional
    void delete(Integer id);

    List<Product> getCart();

    Product addProductToCart(Integer productId, Integer productQuantity);

    void removeProductFromCart(Integer productId);

    Map.Entry<List<Product>, BigDecimal> getCartAndTotalPrice();

    Map.Entry<Product, Integer> getCurrentStockById(Integer id);

    BigDecimal getTotalPrice();

    OrderDetailsDto convertOrderDetailsToDto(OrderDetails orderDetails);

    List<OrderDetailsDto> convertOrderDetailsToDto(List<OrderDetails> ordersDetails);

    List<String> validateOrdersDetails(List<OrderDetails> ordersDetails);

    List<String> validateOrderDetails(OrderDetails orderDetails);
}
