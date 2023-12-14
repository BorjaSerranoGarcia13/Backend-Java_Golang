package com.ecommerce.service;

import java.util.List;

import com.ecommerce.model.Product;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    Product findByReference(String reference);
    List<Product> findProductsByName(String name);
    Product findById(Integer id);
    @Transactional
    Product save(Product product, MultipartFile imageFile, HttpSession session);
    @Transactional
    Product update(Product product, MultipartFile newImageFile, HttpSession session);
    @Transactional
    void saveAllProducts(List<Product> products);
    @Transactional
    void delete(Integer id, HttpSession session);
    List<Product> findAll();
}
