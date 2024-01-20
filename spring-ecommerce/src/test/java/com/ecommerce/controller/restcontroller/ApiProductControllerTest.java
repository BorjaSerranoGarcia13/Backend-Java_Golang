package com.ecommerce.controller.restcontroller;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Product;
import com.ecommerce.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiProductControllerTest {
    @Mock
    private IProductService iProductService;

    private ApiProductController apiProductController;

    @BeforeEach
    void setUp() {
        apiProductController = new ApiProductController(iProductService);
    }

    @Test
    void getAllProductsIncludingDeleted() {
        List<ProductDto> expected = List.of(new ProductDto());
        when(iProductService.findAll()).thenReturn(List.of(new Product()));
        when(iProductService.convertProductToDto(anyList())).thenReturn(expected);

        List<ProductDto> actual = apiProductController.getAllProductsIncludingDeleted();

        assertEquals(expected, actual);
    }

    @Test
    void getAllProductsPaged() {
        Page<Product> productPage = new PageImpl<>(List.of(new Product()));
        Page<ProductDto> expected = productPage.map(iProductService::convertProductToDto);

        when(iProductService.findAll(any(Pageable.class))).thenReturn(productPage);

        Page<ProductDto> actual = apiProductController.getAllProductsPaged(Pageable.unpaged());

        assertEquals(expected.getContent(), actual.getContent());
    }

    @Test
    void getProductById() {
        ProductDto expected = new ProductDto();
        when(iProductService.findById(anyInt())).thenReturn(new Product());
        when(iProductService.convertProductToDto(any(Product.class))).thenReturn(expected);

        ProductDto actual = apiProductController.getProductById(1);

        assertEquals(expected, actual);
    }

    @Test
    void getProductByExactReference() {
        ProductDto expected = new ProductDto();
        when(iProductService.findByReference(anyString())).thenReturn(new Product());
        when(iProductService.convertProductToDto(any(Product.class))).thenReturn(expected);

        ProductDto actual = apiProductController.getProductByExactReference("reference");

        assertEquals(expected, actual);
    }

    @Test
    void getProductsByPartialReference() {
        List<ProductDto> expected = List.of(new ProductDto());
        when(iProductService.findProductsByReference(anyString())).thenReturn(List.of(new Product()));
        when(iProductService.convertProductToDto(anyList())).thenReturn(expected);

        List<ProductDto> actual = apiProductController.getProductsByPartialReference("reference");

        assertEquals(expected, actual);
    }

    @Test
    void searchProductsByNameOrReference() {
        List<ProductDto> expected = List.of(new ProductDto());
        when(iProductService.searchProductsByNameOrReference(anyString(), anyString())).thenReturn(List.of(new Product()));
        when(iProductService.convertProductToDto(anyList())).thenReturn(expected);

        List<ProductDto> actual = apiProductController.searchProductsByNameOrReference("searchTerm", "searchType");

        assertEquals(expected, actual);
    }

    @Test
    void saveProduct() {
        ProductDto expected = new ProductDto();
        when(iProductService.save(any(ProductDto.class))).thenReturn(new Product());
        when(iProductService.convertProductToDto(any(Product.class))).thenReturn(expected);

        ProductDto actual = apiProductController.saveProduct(new ProductDto());

        assertEquals(expected, actual);
    }

    @Test
    void updateProduct() {
        ProductDto expected = new ProductDto();
        when(iProductService.update(any(ProductDto.class))).thenReturn(new Product());
        when(iProductService.convertProductToDto(any(Product.class))).thenReturn(expected);

        ProductDto actual = apiProductController.updateProduct(new ProductDto());

        assertEquals(expected, actual);
    }

    @Test
    void deleteProduct() {
        int id = 1;

        apiProductController.deleteProduct(id);

        verify(iProductService, times(1)).delete(id);
    }
}