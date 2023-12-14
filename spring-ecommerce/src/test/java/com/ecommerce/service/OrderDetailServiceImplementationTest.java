package com.ecommerce.service;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.IOrderDetailRepository;
import com.ecommerce.utils.UserSessionUtils;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@DataJpaTest
public class OrderDetailServiceImplementationTest {
    @Mock
    private IOrderDetailRepository iOrderDetailRepository;

    @Mock
    private IProductService iProductService;
    @Mock
    private IOrderService iOrderService;

    @Mock
    private HttpSession session;

    @Mock
    private UserSessionUtils userSessionUtils;

    private OrderDetailServiceImplementation orderDetailService;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        orderDetailService = new OrderDetailServiceImplementation(iOrderDetailRepository, iProductService, iOrderService, userSessionUtils);
    }

    @Test
    public void testSetAndGetOrderDetailsList() {
        OrderDetails orderDetail1 = new OrderDetails();
        OrderDetails orderDetail2 = new OrderDetails();
        List<OrderDetails> orderDetails = Arrays.asList(orderDetail1, orderDetail2);

        orderDetailService.setOrderDetailsList(orderDetails);
        List<OrderDetails> result = orderDetailService.getOrderDetailsList();

        assertEquals(orderDetails, result);
    }

    @Test
    public void testGetCurrentOrderDetailsProductQuantity() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);

        OrderDetails orderDetail = new OrderDetails("DetailRef", 3, product.getPrice(), BigDecimal.valueOf(3).multiply(product.getPrice()), product);

        orderDetailService.addOrderDetailList(orderDetail);

        Integer quantity = orderDetailService.getCurrentOrderDetailsProductQuantity("ProductRef");

        assertEquals(3, quantity);
    }

    @Test
    public void testAddOrderDetailList() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);

        OrderDetails orderDetail = new OrderDetails("DetailRef", 3, product.getPrice(), BigDecimal.valueOf(3).multiply(product.getPrice()), product);

        orderDetailService.addOrderDetailList(orderDetail);
        List<OrderDetails> result = orderDetailService.getOrderDetailsList();

        assertEquals(1, result.size());
        assertEquals(orderDetail, result.get(0));
    }

    @Test
    public void testFindByReference() {
        OrderDetails orderDetail = new OrderDetails();
        orderDetail.setReference("DrderRef");

        when(iOrderDetailRepository.findByReference("DrderRef")).thenReturn(List.of(orderDetail));

        List<OrderDetails> result = orderDetailService.findByReference("DrderRef");

        assertEquals(1, result.size());
        assertEquals(orderDetail, result.get(0));
    }

    @Test
    public void testFindById() {
        OrderDetails orderDetail = new OrderDetails();
        orderDetail.setId(1);

        when(iOrderDetailRepository.findById(1)).thenReturn(Optional.of(orderDetail));

        OrderDetails result = orderDetailService.findById(1);

        assertEquals(orderDetail, result);
    }

    @Test
    public void testSave() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(1.00), 10, null);

        OrderDetails orderDetail = new OrderDetails("DetailRef", 1, product.getPrice(), BigDecimal.valueOf(3).multiply(product.getPrice()), product);

        orderDetailService.addOrderDetailList(orderDetail);

        User user = new User();
        when(userSessionUtils.checkVerifiedUserFromSession(session)).thenReturn(Optional.of(user));

        Order order = new Order();
        when(iOrderService.save(any(Order.class))).thenReturn(order);

        assertDoesNotThrow(() -> orderDetailService.save(session));

        verify(iOrderDetailRepository, times(1)).saveAll(anyList());
        verify(iProductService, times(1)).saveAllProducts(anyList());
    }

    @Test
    public void testDelete() {
        User admin = new User(1, "name", "username", "password", "email", "address", "123", true);

        when(userSessionUtils.checkVerifiedAdminFromSession(session)).thenReturn(Optional.of(admin));

        assertDoesNotThrow(() -> orderDetailService.delete(1, session));
        verify(iOrderDetailRepository, times(1)).deleteById(1);
    }

    @Test
    public void testAddProductToOrderDetailList() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);

        when(iProductService.findByReference("ProductRef")).thenReturn(product);

        assertDoesNotThrow(() -> orderDetailService.addProductToOrderDetailList("ProductRef", 5));
        assertEquals(1, orderDetailService.getOrderDetailsList().size());
    }

    @Test
    public void testUpdateOrderDetailList() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(1.00), 10, null);

        OrderDetails orderDetail = new OrderDetails("DetailRef", 1, product.getPrice(), BigDecimal.valueOf(3).multiply(product.getPrice()), product);

        orderDetailService.addOrderDetailList(orderDetail);

        assertDoesNotThrow(() -> orderDetailService.updateOrderDetailList(orderDetail.getProduct(), orderDetail.getQuantity()));
        assertEquals(BigDecimal.valueOf(1.00), orderDetailService.getOrderDetailsList().get(0).getPrice());
    }

    @Test
    public void testDeleteProductInOrderDetailList() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);

        OrderDetails orderDetail = new OrderDetails("DetailRef", 3, product.getPrice(), BigDecimal.valueOf(3).multiply(product.getPrice()), product);

        orderDetailService.addOrderDetailList(orderDetail);

        assertDoesNotThrow(() -> orderDetailService.deleteProductInOrderDetailList(1));
        assertTrue(orderDetailService.getOrderDetailsList().isEmpty());
    }

    @Test
    public void testGetTotalPrice() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);

        OrderDetails orderDetail = new OrderDetails("DetailRef", 3, product.getPrice(), BigDecimal.valueOf(3).multiply(product.getPrice()), product);

        orderDetailService.addOrderDetailList(orderDetail);

        BigDecimal totalPrice = orderDetailService.getTotalPrice();

        assertEquals(BigDecimal.valueOf(30.00), totalPrice);
    }
}
