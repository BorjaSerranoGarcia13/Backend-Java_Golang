package com.ecommerce.controller.restcontroller;

import com.ecommerce.dto.OrderDetailsDto;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.service.IOrderDetailsService;
import com.ecommerce.service.IOrderService;
import com.ecommerce.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiOrderDetailsControllerTest {
    @Mock
    private IOrderDetailsService iOrderDetailsService;
    @Mock
    private IProductService iProductService;
    @Mock
    private IOrderService iOrderService;

    private ApiOrderDetailsController apiOrderDetailsController;

    @BeforeEach
    void setUp() {
        apiOrderDetailsController = new ApiOrderDetailsController(iOrderDetailsService, iProductService, iOrderService);
    }

    @Test
    void getAllOrderDetails() {
        List<OrderDetailsDto> expected = List.of(new OrderDetailsDto());
        when(iOrderDetailsService.findAll()).thenReturn(List.of(new OrderDetails()));
        when(iOrderDetailsService.convertOrderDetailsToDto(anyList())).thenReturn(expected);

        List<OrderDetailsDto> actual = apiOrderDetailsController.getAllOrderDetails();

        assertEquals(expected, actual);
    }

    @Test
    void getAllOrderDetailsPaged() {
        Page<OrderDetails> orderDetailsPage = new PageImpl<>(List.of(new OrderDetails()));
        Page<OrderDetailsDto> expected = orderDetailsPage.map(iOrderDetailsService::convertOrderDetailsToDto);

        when(iOrderDetailsService.findAll(any(Pageable.class))).thenReturn(orderDetailsPage);

        Page<OrderDetailsDto> actual = apiOrderDetailsController.getAllOrderDetailsPaged(Pageable.unpaged());

        assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    void getOrderDetailsById() {
        OrderDetailsDto expected = new OrderDetailsDto();
        when(iOrderDetailsService.findById(anyInt())).thenReturn(new OrderDetails());
        when(iOrderDetailsService.convertOrderDetailsToDto(any(OrderDetails.class))).thenReturn(expected);

        OrderDetailsDto actual = apiOrderDetailsController.getOrderDetailsById(1);

        assertEquals(expected, actual);
    }

    @Test
    void getProductAndStockInCurrentCart_productExists() {
        // Arrange
        Product product = new Product();
        ProductDto productDto = new ProductDto();
        when(iOrderDetailsService.getCurrentStockById(anyInt())).thenReturn(new AbstractMap.SimpleEntry<>(product, 1));
        when(iProductService.convertProductToDto(product)).thenReturn(productDto);

        // Act
        Map.Entry<ProductDto, Integer> actual = apiOrderDetailsController.getProductAndStockInCurrentCart(1);

        // Assert
        assertEquals(new AbstractMap.SimpleEntry<>(productDto, 1), actual);
    }

    @Test
    void getCartAndTotalPrice_cartNotEmpty() {
        List<Product> products = Collections.singletonList(new Product());
        List<ProductDto> productDtos = Collections.singletonList(new ProductDto());
        BigDecimal totalPrice = BigDecimal.TEN;
        Map.Entry<List<Product>, BigDecimal> productsAndPrice = new AbstractMap.SimpleEntry<>(products, totalPrice);

        when(iOrderDetailsService.getCartAndTotalPrice()).thenReturn(productsAndPrice);
        when(iProductService.convertProductToDto(products)).thenReturn(productDtos);

        Map.Entry<List<ProductDto>, BigDecimal> actual = apiOrderDetailsController.getCartAndTotalPrice();

        // Assert
        assertEquals(new AbstractMap.SimpleEntry<>(productDtos, totalPrice), actual);
    }

    @Test
    void getCartAndTotalPrice_cartEmpty() {
        List<Product> products = Collections.emptyList();
        List<ProductDto> productDtos = Collections.emptyList();
        BigDecimal totalPrice = BigDecimal.ZERO;
        Map.Entry<List<Product>, BigDecimal> productsAndPrice = new AbstractMap.SimpleEntry<>(products, totalPrice);

        when(iOrderDetailsService.getCartAndTotalPrice()).thenReturn(productsAndPrice);
        when(iProductService.convertProductToDto(products)).thenReturn(productDtos);

        Map.Entry<List<ProductDto>, BigDecimal> actual = apiOrderDetailsController.getCartAndTotalPrice();

        assertEquals(new AbstractMap.SimpleEntry<>(productDtos, totalPrice), actual);
    }

    @Test
    void addProductToCart() {
        ProductDto expected = new ProductDto();
        when(iOrderDetailsService.addProductToCart(anyInt(), anyInt())).thenReturn(new Product());
        when(iProductService.convertProductToDto(any(Product.class))).thenReturn(expected);

        ProductDto actual = apiOrderDetailsController.addProductToCart(1, 1);

        assertEquals(expected, actual);
    }

    @Test
    void removeProductFromCart() {
        apiOrderDetailsController.removeProductFromCart(1);

        verify(iOrderDetailsService, times(1)).removeProductFromCart(1);
    }

    @Test
    void confirmPurchaseAndCreateOrder() {
        OrderDto expected = new OrderDto();
        when(iOrderDetailsService.save()).thenReturn(new Order());
        when(iOrderService.convertOrderToDto(any(Order.class))).thenReturn(expected);

        OrderDto actual = apiOrderDetailsController.confirmPurchaseAndCreateOrder();

        assertEquals(expected, actual);
    }
}