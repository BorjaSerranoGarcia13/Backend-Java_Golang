package com.ecommerce.utils;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

    @Test
    public void testValidateOrder() {
        Order order = new Order();
        order.setReference("DOrderRef");
        order.setCreationDate(new Date());
        order.setTotal(BigDecimal.TEN);
        order.setOrderDetails(List.of(new OrderDetails()));
        order.setUser(new User());

        List<String> errors = Validator.validateOrder(order);

        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateOrderDetails() {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setProduct(new Product());
        orderDetails.setPrice(BigDecimal.TEN);
        orderDetails.setQuantity(1);
        orderDetails.setTotal(BigDecimal.TEN);

        List<String> errors = Validator.validateOrderDetails(orderDetails);

        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateProduct() {
        Product product = new Product();
        product.setName("Product Name");
        product.setDescription("Product Description");
        product.setPrice(BigDecimal.TEN);
        product.setQuantity(1);

        List<String> errors = Validator.validateProduct(product);

        assertTrue(errors.isEmpty());
    }

    @Test
    public void testValidateRegisterUser() {
        User user = new User(1, "name","username", "password", "email", "phoneNumber", "address", true);
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("user@example.com");
        user.setPhoneNumber("1234567890");
        user.setAddress("User Address");

        List<String> errors = Validator.validateRegisterUser(user);

        assertTrue(errors.isEmpty());
    }
}