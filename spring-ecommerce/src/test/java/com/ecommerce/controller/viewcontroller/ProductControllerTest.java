package com.ecommerce.controller.viewcontroller;

import com.ecommerce.controller.viewcontroller.ProductController;
import com.ecommerce.controller.restcontroller.ApiProductController;
import com.ecommerce.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.Model;

import static com.ecommerce.constants.redirect.RedirectConstants.REDIRECT_PRODUCTS;
import static com.ecommerce.constants.view.ViewConstants.ADMIN_CREATE_VIEW;
import static com.ecommerce.constants.view.ViewConstants.ADMIN_EDIT_VIEW;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ApiProductController apiProductController;

    @Mock
    private Model model;

    @InjectMocks
    private ProductController productController;

    @Test
    void create() {
        String view = productController.create();

        assertEquals(ADMIN_CREATE_VIEW, view);
    }

    @Test
    void edit() {
        ProductDto product = new ProductDto();
        when(apiProductController.getProductById(anyInt())).thenReturn(product);

        String view = productController.edit(1, model);

        assertEquals(ADMIN_EDIT_VIEW, view);
        verify(model, times(1)).addAttribute("product", product);
    }

    @Test
    void save() {
        ProductDto product = new ProductDto();
        when(apiProductController.saveProduct(product)).thenReturn(product);

        String view = productController.save(product);

        assertEquals(REDIRECT_PRODUCTS, view);
    }

    @Test
    void update() {
        ProductDto product = new ProductDto();
        when(apiProductController.updateProduct(product)).thenReturn(product);

        String view = productController.update(product);

        assertEquals(REDIRECT_PRODUCTS, view);
    }

    @Test
    void delete() {
        doNothing().when(apiProductController).deleteProduct(anyInt());

        String view = productController.delete(1);

        assertEquals(REDIRECT_PRODUCTS, view);
    }
}
