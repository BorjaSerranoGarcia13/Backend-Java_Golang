package com.ecommerce.controller.viewcontroller;

import com.ecommerce.controller.restcontroller.ApiUserController;
import com.ecommerce.controller.viewcontroller.HomeController;
import com.ecommerce.controller.restcontroller.ApiOrderDetailsController;
import com.ecommerce.controller.restcontroller.ApiProductController;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.ProductDto;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USER_CART;
import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USER_HOME;
import static com.ecommerce.constants.view.ViewConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HomeControllerTest {

    @Mock
    private ApiProductController apiProductController;

    @Mock
    private ApiOrderDetailsController apiOrderDetailsController;

    @Mock
    private ApiUserController apiUserController;

    @Mock
    private Model model;

    @InjectMocks
    private HomeController homeController;

    @Test
    void home() {
        Page<ProductDto> productPage = new PageImpl<>(Collections.emptyList());
        when(apiProductController.getAllProductsPaged(any())).thenReturn(productPage);

        String view = homeController.home(model, 0);

        assertEquals(USER_HOME_VIEW, view);
        verify(model, times(1)).addAttribute("products", productPage.getContent());
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", productPage.getTotalPages());
    }

    @Test
    void showProductById() {
        Map.Entry<ProductDto, Integer> productAndQuantity = Map.entry(new ProductDto(), 1);
        when(apiOrderDetailsController.getProductAndStockInCurrentCart(anyInt())).thenReturn(productAndQuantity);

        String view = homeController.showProductById(1, model);

        assertEquals(USER_PRODUCT_VIEW, view);
        verify(model, times(1)).addAttribute("product", productAndQuantity.getKey());
        verify(model, times(1)).addAttribute("stock", productAndQuantity.getValue());
    }

    @Test
    void addProductToCart() {
        ProductDto P = new ProductDto();
        when(apiOrderDetailsController.addProductToCart(anyInt(), anyInt())).thenReturn(P);

        String view = homeController.addProductToCart(1, 1);

        assertEquals(REDIRECT_USER_HOME, view);
    }

    @Test
    void showCart() {
        Map.Entry<List<ProductDto>, BigDecimal> productAndTotalPrice = Map.entry(Collections.emptyList(), BigDecimal.ZERO);
        when(apiOrderDetailsController.getCartAndTotalPrice()).thenReturn(productAndTotalPrice);

        String view = homeController.showCart(model);

        assertEquals(USER_CART_VIEW, view);
        verify(model, times(1)).addAttribute("products", productAndTotalPrice.getKey());
        verify(model, times(1)).addAttribute("totalPrice", productAndTotalPrice.getValue());
    }

    @Test
    void deleteProductCart() {
        doNothing().when(apiOrderDetailsController).removeProductFromCart(anyInt());

        String view = homeController.deleteProductCart(1);

        assertEquals(REDIRECT_USER_CART, view);
    }

    @Test
    void confirmPurchaseAndSaveOrderDetails() {
        OrderDto order = new OrderDto();
        when(apiOrderDetailsController.confirmPurchaseAndCreateOrder()).thenReturn(order);

        String view = homeController.confirmPurchaseAndSaveOrderDetails();

        assertEquals(REDIRECT_USER_HOME, view);
    }

    @Test
    void orderSummary() {
        UserDto user = new UserDto();
        Map.Entry<List<ProductDto>, BigDecimal> productsAndTotalPrice = Map.entry(Collections.emptyList(), BigDecimal.ZERO);
        when(apiUserController.getUserById(anyInt())).thenReturn(user);
        when(apiOrderDetailsController.getCartAndTotalPrice()).thenReturn(productsAndTotalPrice);

        String view = homeController.orderSummary(model);

        assertEquals(USER_ORDER_VIEW, view);
        verify(model, times(1)).addAttribute("user", user);
        verify(model, times(1)).addAttribute("products", productsAndTotalPrice.getKey());
        verify(model, times(1)).addAttribute("totalPrice", productsAndTotalPrice.getValue());
    }

    @Test
    void searchProductsByNameOrReference() {
        List<ProductDto> products = Collections.emptyList();
        when(apiProductController.searchProductsByNameOrReference(anyString(), anyString())).thenReturn(products);

        String view = homeController.searchProductsByNameOrReference("searchTerm", "searchType", model);

        assertEquals(USER_HOME_VIEW, view);
        verify(model, times(1)).addAttribute("products", products);
        verify(model, times(1)).addAttribute("currentPage", 0);
        verify(model, times(1)).addAttribute("totalPages", 1);
    }
}
