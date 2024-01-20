package com.ecommerce.controller.viewcontroller;

import com.ecommerce.controller.restcontroller.ApiUserController;
import com.ecommerce.controller.restcontroller.ApiProductController;
import com.ecommerce.controller.restcontroller.ApiOrderController;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private ApiProductController apiProductController;

    @Mock
    private ApiOrderController apiOrderController;

    @Mock
    private ApiUserController apiUserController;

    @Mock
    private Model model;

    @InjectMocks
    private AdminController adminController;

    @Test
    void homeView_productsExist() {
        List<ProductDto> products = Collections.singletonList(new ProductDto());
        Page<ProductDto> productPage = new PageImpl<>(products);
        when(apiProductController.getAllProductsPaged(any())).thenReturn(productPage);

        String view = adminController.homeView(model, 0);

        assertEquals("admin/home", view);
        verify(model, times(1)).addAttribute("products", products);
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", productPage.getTotalPages());
    }

    @Test
    void homeView_noProducts() {
        List<ProductDto> products = Collections.emptyList();
        Page<ProductDto> productPage = new PageImpl<>(products);
        when(apiProductController.getAllProductsPaged(any())).thenReturn(productPage);

        String view = adminController.homeView(model, 0);

        assertEquals("admin/home", view);
        verify(model, times(1)).addAttribute("products", products);
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", productPage.getTotalPages());
    }

    @Test
    void productsView_productsExist() {
        List<ProductDto> products = Collections.singletonList(new ProductDto());
        Page<ProductDto> productPage = new PageImpl<>(products);
        when(apiProductController.getAllProductsPaged(any())).thenReturn(productPage);

        String view = adminController.productsView(model, 0);

        assertEquals("admin/products", view);
        verify(model, times(1)).addAttribute("products", products);
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", productPage.getTotalPages());
    }

    @Test
    void productsView_noProducts() {
        List<ProductDto> products = Collections.emptyList();
        Page<ProductDto> productPage = new PageImpl<>(products);
        when(apiProductController.getAllProductsPaged(any())).thenReturn(productPage);

        String view = adminController.productsView(model, 0);

        assertEquals("admin/products", view);
        verify(model, times(1)).addAttribute("products", products);
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", productPage.getTotalPages());
    }

    @Test
    void productHomeView_productExists() {
        ProductDto product = new ProductDto();
        when(apiProductController.getProductById(anyInt())).thenReturn(product);

        String view = adminController.productHomeView(1, model);

        assertEquals("admin/producthome", view);
        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void ordersView_ordersExist() {
        List<OrderDto> orders = Collections.singletonList(new OrderDto());
        Page<OrderDto> orderPage = new PageImpl<>(orders);
        when(apiOrderController.getAllOrdersPaged(any())).thenReturn(orderPage);

        String view = adminController.ordersView(model, 0);

        assertEquals("admin/orders", view);
        verify(model, times(1)).addAttribute("orders", orders);
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", orderPage.getTotalPages());
    }

    @Test
    void ordersView_noOrders() {
        List<OrderDto> orders = Collections.emptyList();
        Page<OrderDto> orderPage = new PageImpl<>(orders);
        when(apiOrderController.getAllOrdersPaged(any())).thenReturn(orderPage);

        String view = adminController.ordersView(model, 0);

        assertEquals("admin/orders", view);
        verify(model, times(1)).addAttribute("orders", orders);
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", orderPage.getTotalPages());
    }

    @Test
    void orderDetailsView() {
        OrderDto order = new OrderDto();
        when(apiOrderController.getOrderById(anyInt())).thenReturn(order);

        String view = adminController.orderDetailsView(1, model);

        assertEquals("admin/orderdetail", view);
        verify(model, times(1)).addAttribute("totalOrder", order.getTotal());
        verify(model, times(1)).addAttribute("order", order);
    }

    @Test
    void usersView() {
        Page<UserDto> userPage = new PageImpl<>(Collections.emptyList());
        when(apiUserController.getAllUsersPaged(any())).thenReturn(userPage);

        String view = adminController.usersView(model, 0);

        assertEquals("admin/users", view);
        verify(model, times(1)).addAttribute("users", userPage.getContent());
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", userPage.getTotalPages());
    }

    @Test
    void usersDelete() {
        String view = adminController.usersDelete(1, model);

        assertEquals("redirect:/admin/users/list", view);
        verify(apiUserController, times(1)).deleteUserById(1);
    }
}