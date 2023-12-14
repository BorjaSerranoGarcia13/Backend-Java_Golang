package com.ecommerce.controller;

import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.IOrderDetailService;
import com.ecommerce.service.IOrderService;
import com.ecommerce.service.IProductService;
import com.ecommerce.service.IUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USERS_VIEW;
import static com.ecommerce.constants.view.ViewConstants.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class AdminControllerTest {

    @Mock
    private IProductService iProductService;

    @Mock
    private IOrderService iOrderService;

    @Mock
    private IOrderDetailService iOrderDetailService;

    @Mock
    private IUserService iUserService;
    private Model model;

    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        adminController = new AdminController(iProductService, iOrderService, iOrderDetailService, iUserService);
        model = new BindingAwareModelMap();
    }

    @Test
    public void testHomeView() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(iProductService.findAll()).thenReturn(products);

        String view = adminController.homeView(model);

        assertEquals(ADMIN_HOME_VIEW, view);
        assertEquals(products, model.getAttribute("products"));
    }

    @Test
    public void testProductView() {
        String reference = "testReference";
        Product product = new Product();

        when(iProductService.findByReference(reference)).thenReturn(product);

        String view = adminController.productView(reference, model);

        assertEquals(PRODUCT_VIEW, view);
        assertEquals(product, model.getAttribute("product"));
    }

    @Test
    public void testOrdersView() {
        List<Order> orders = Arrays.asList(new Order(), new Order());
        when(iOrderService.findAll()).thenReturn(orders);

        String view = adminController.ordersView(model);

        assertEquals(ADMIN_ORDERS_VIEW, view);
        assertEquals(orders, model.getAttribute("orders"));
    }

    @Test
    public void testOrderDetailView() {
        String reference = "testReference";
        List<OrderDetails> orderDetails = Arrays.asList(new OrderDetails(), new OrderDetails());
        when(iOrderDetailService.findByReference(reference)).thenReturn(orderDetails);

        String view = adminController.orderDetailView(reference, model);

        assertEquals(ADMIN_ORDER_DETAIL_VIEW, view);
        assertEquals(orderDetails, model.getAttribute("orderDetails"));
    }

    @Test
    public void testUsersView() {
        List<User> users = Arrays.asList(new User(), new User());
        when(iUserService.findAll()).thenReturn(users);

        String view = adminController.usersView(model);

        assertEquals(ADMIN_USERS_VIEW, view);
        assertEquals(users, model.getAttribute("users"));
    }

    @Test
    public void testUsersDelete() {
        Integer id = 1;

        doNothing().when(iUserService).delete(id);

        String view = adminController.usersDelete(id, model);

        assertEquals(REDIRECT_USERS_VIEW, view);
    }
}

