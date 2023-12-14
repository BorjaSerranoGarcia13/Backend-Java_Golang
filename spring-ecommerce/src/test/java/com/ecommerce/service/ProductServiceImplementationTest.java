package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.IProductRepository;
import com.ecommerce.utils.UserSessionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceImplementationTest {

    @Mock
    private IProductRepository iProductRepository;

    @Mock
    private UserSessionUtils userSessionUtils;

    @Mock
    private HttpSession session;

    @Mock
    private UploadFileService uploadFileService;

    private ProductServiceImplementation productService;

    @BeforeEach
    public void setup() {
        assertDoesNotThrow(() -> MockitoAnnotations.openMocks(this));
        productService = new ProductServiceImplementation(iProductRepository, uploadFileService, userSessionUtils);
    }

    @Test
    public void testFindByReference() {
        Product product = new Product();
        when(iProductRepository.findByReference("ProductRef")).thenReturn(Optional.of(product));

        Product result = productService.findByReference("ProductRef");

        assertEquals(product, result);
        verify(iProductRepository, times(1)).findByReference("ProductRef");
    }

    @Test
    public void testFindProductsByName() {
        Product product = new Product();
        product.setName("product");
        when(iProductRepository.findProductsByName("product")).thenReturn(List.of(product));

        List<Product> result = productService.findProductsByName("product");

        assertEquals(1, result.size());
        assertEquals(product, result.get(0));
    }

    @Test
    public void testFindById() {
        Product product = new Product();
        when(iProductRepository.findById(1)).thenReturn(Optional.of(product));

        Product result = productService.findById(1);

        assertEquals(product, result);
        verify(iProductRepository, times(1)).findById(1);
    }

    @Test
    public void testSave() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);

        MultipartFile imageFile = new MockMultipartFile("imageFile", "hello.png", "image/png", "some image".getBytes());
        User user = new User();

        when(uploadFileService.saveImage(imageFile)).thenReturn("hello.png");
        when(userSessionUtils.checkVerifiedAdminFromSession(session)).thenReturn(Optional.of(user));
        when(iProductRepository.save(product)).thenReturn(product);

        Product result = productService.save(product, imageFile, session);

        assertEquals(product, result);
        verify(iProductRepository, times(1)).save(product);
        verify(uploadFileService, times(1)).saveImage(imageFile);
        verify(userSessionUtils, times(1)).checkVerifiedAdminFromSession(session);
    }

    @Test
    public void testUpdate() {
        Product product = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);
        MultipartFile imageFile = new MockMultipartFile("imageFile", "hello.png", "image/png", "some image".getBytes());
        User user = new User();
        user.setAdmin(true);

        when(uploadFileService.saveImage(imageFile)).thenReturn("hello.png");
        when(userSessionUtils.checkVerifiedAdminFromSession(session)).thenReturn(Optional.of(user));
        when(iProductRepository.save(product)).thenReturn(product);

        Product result = productService.update(product, imageFile, session);

        assertEquals(product, result);
        verify(iProductRepository, times(1)).save(product);
        verify(uploadFileService, times(1)).saveImage(imageFile);
        verify(userSessionUtils, times(1)).checkVerifiedAdminFromSession(session);
    }

    @Test
    public void testSaveAllProducts() {
        Product product1 = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);

        Product product2 = new Product(1, "ProductRef", "name", "description", null, BigDecimal.valueOf(10.00), 10, null);

        List<Product> products = Arrays.asList(product1, product2);

        assertDoesNotThrow(() -> productService.saveAllProducts(products));
        verify(iProductRepository, times(1)).saveAll(products);
    }

    @Test
    public void testDelete() {
        Product product = new Product(1, "ProductRef", "name", "description", "image.png", BigDecimal.valueOf(10.00), 10, null);

        User user = new User();
        user.setAdmin(true);

        when(iProductRepository.findById(1)).thenReturn(Optional.of(product));
        when(userSessionUtils.checkVerifiedAdminFromSession(session)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> productService.delete(1, session));
        verify(iProductRepository, times(1)).deleteById(1);
        verify(uploadFileService, times(1)).deleteImage(product.getImage());
    }

    @Test
    public void testFindAll() {
        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = Arrays.asList(product1, product2);
        when(iProductRepository.findAll()).thenReturn(products);

        List<Product> result = productService.findAll();

        assertEquals(products, result);
        verify(iProductRepository, times(1)).findAll();
    }
}
