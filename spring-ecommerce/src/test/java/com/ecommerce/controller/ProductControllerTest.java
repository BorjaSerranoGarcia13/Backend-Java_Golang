package com.ecommerce.controller;

import com.ecommerce.model.Product;
import com.ecommerce.service.IProductService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_PRODUCTS;
import static com.ecommerce.constants.view.ViewConstants.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class ProductControllerTest {

    @Mock
    private IProductService iProductService;

    @Mock
    private MultipartFile file;

    @Mock
    private HttpSession session;
    private Model model;
    private ProductController productController;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        productController = new ProductController(iProductService);
        model = new BindingAwareModelMap();
    }

    @Test
    public void testShow() {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);

        when(iProductService.findAll()).thenReturn(products);

        String viewName = productController.show(model);

        assertEquals(SHOW_VIEW, viewName);
        assertEquals(products, model.getAttribute("products"));
    }

    @Test
    public void testCreate() {
        String viewName = productController.create();
        assertEquals(CREATE_VIEW, viewName);
    }

    @Test
    public void testSave() {
        Product product = new Product();

        String viewName = productController.save(product, file, session);

        assertEquals(REDIRECT_PRODUCTS, viewName);
        verify(iProductService, times(1)).save(product, file, session);
    }

    @Test
    public void testEdit() {
        Product product = new Product();
        String reference = "ProductRef";

        when(iProductService.findByReference(reference)).thenReturn(product);

        String viewName = productController.edit(reference, model);

        assertEquals(EDIT_VIEW, viewName);
        assertEquals(product, model.getAttribute("product"));
    }

    @Test
    public void testUpdate() {
        Product product = new Product();

        String viewName = productController.update(product, file, session);

        assertEquals(REDIRECT_PRODUCTS, viewName);
        verify(iProductService, times(1)).update(product, file, session);
    }

    @Test
    public void testDelete() {
        Integer id = 1;

        String viewName = productController.delete(id, session);

        assertEquals(REDIRECT_PRODUCTS, viewName);
        verify(iProductService, times(1)).delete(id, session);
    }
}
