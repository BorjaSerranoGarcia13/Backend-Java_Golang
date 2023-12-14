package com.ecommerce.controller;

import com.ecommerce.model.OrderDetails;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.service.IOrderDetailService;
import com.ecommerce.service.IProductService;
import com.ecommerce.service.IUserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static com.ecommerce.constants.SessionAttributes.USER_ID;
import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USER_CART;
import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_USER_HOME;
import static com.ecommerce.constants.view.ViewConstants.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HomeControllerTest {

    @Mock
    private IProductService iProductService;

    private Model model;

    @Mock
    private IOrderDetailService iOrderDetailService;

    @Mock
    private IUserService iUserService;

    private HomeController homeController;

    @BeforeEach
    public void setUp() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        homeController = new HomeController(iProductService, iOrderDetailService, iUserService);
        model = new BindingAwareModelMap();
    }

    @Test
    public void testHome() {
        List<Product> products = Arrays.asList(new Product(), new Product());
        when(iProductService.findAll()).thenReturn(products);

        String view = homeController.home(model, null);

        assertEquals(USER_HOME_VIEW, view);
        assertEquals(products, model.getAttribute("products"));
    }

    @Test
    public void testProduct() {
        String reference = "testReference";
        Product product = new Product();
        product.setQuantity(0);

        when(iProductService.findByReference(reference)).thenReturn(product);
        when(iOrderDetailService.getCurrentOrderDetailsProductQuantity(reference)).thenReturn(0);

        String view = homeController.product(reference, model);

        assertEquals(USER_PRODUCT_VIEW, view);
        assertEquals(product, model.getAttribute("product"));
        assertEquals(0, model.getAttribute("stock"));
    }

    @Test
    public void testAddToCart() {
        String reference = "testReference";
        Integer quantity = 1;

        String view = homeController.addToCart(reference, quantity);

        verify(iOrderDetailService, times(1)).addProductToOrderDetailList(reference, quantity);

        assertEquals(REDIRECT_USER_HOME, view);

    }

    @Test
    public void testCart() {
        List<OrderDetails> orderDetails = Arrays.asList(new OrderDetails(), new OrderDetails());
        BigDecimal totalPrice = new BigDecimal("100.00");

        when(iOrderDetailService.getOrderDetailsList()).thenReturn(orderDetails);
        when(iOrderDetailService.getTotalPrice()).thenReturn(totalPrice);

        String view = homeController.cart(model);

        assertEquals(USER_CART_VIEW, view);
        assertEquals(orderDetails, model.getAttribute("orderDetails"));
        assertEquals(totalPrice, model.getAttribute("totalPrice"));
    }

    @Test
    public void testDeleteProductCart() {
        Integer id = 1;

        String view = homeController.deleteProductCart(id);

        assertEquals(REDIRECT_USER_CART, view);

        verify(iOrderDetailService, times(1)).deleteProductInOrderDetailList(id);
    }

    @Test
    public void testSaveOrderDetails() {
        HttpSession session = new MockHttpSession();

        String view = homeController.saveOrderDetails(session);

        assertEquals(REDIRECT_USER_HOME, view);

        verify(iOrderDetailService, times(1)).save(session);
    }

    @Test
    public void testOrderSummary() {
        List<OrderDetails> orderDetails = getOrderDetails();

        HttpSession session = new MockHttpSession();
        session.setAttribute(USER_ID, 1);
        User user = new User(1, "test", "test", "test", "test", "test", "test", false);

        when(iOrderDetailService.getOrderDetailsList()).thenReturn(orderDetails);
        when(iUserService.findById(1)).thenReturn(user); 

        String view = homeController.orderSummary(model, session);

        assertEquals(USER_ORDER_VIEW, view);
        assertEquals(orderDetails, model.getAttribute("orderDetails"));
        assertEquals(user, model.getAttribute("user"));
        assertEquals(new BigDecimal("20.00"), model.getAttribute("total"));
    }

    private static List<OrderDetails> getOrderDetails() {
        Product product = new Product();
        product.setPrice(new BigDecimal("10.00"));

        OrderDetails orderDetail1 = new OrderDetails();
        orderDetail1.setProduct(product);
        orderDetail1.setQuantity(1);
        orderDetail1.setPrice(new BigDecimal("10.00"));

        OrderDetails orderDetail2 = new OrderDetails();
        orderDetail2.setProduct(product);
        orderDetail2.setQuantity(1);
        orderDetail1.setPrice(new BigDecimal("10.00"));

        return Arrays.asList(orderDetail1, orderDetail2);
    }

    @Test
    public void testSearchProductByName() {
        Product product1 = new Product();
        product1.setName("Product 1");
        Product product2 = new Product();
        product2.setName("Product 2");
        List<Product> products = Arrays.asList(product1, product2);

        when(iProductService.findProductsByName(anyString())).thenReturn(products);

        String viewName = homeController.searchProduct("Product", "name", model);

        assertEquals(USER_HOME_VIEW, viewName);
    }

    @Test
    public void testSearchProductByReference() {
        Product product = new Product();
        product.setReference("ProductRef");

        when(iProductService.findByReference(anyString())).thenReturn(product);

        String viewName = homeController.searchProduct("ProductRef", "reference", model);

        assertEquals(USER_HOME_VIEW, viewName);
    }
}