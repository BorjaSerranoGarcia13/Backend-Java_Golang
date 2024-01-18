package com.ecommerce.service;

import com.ecommerce.dto.ProductDto;
import com.ecommerce.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface IProductService {
    List<Product> findAll();

    Page<Product> findAll(Pageable pageable);

    Product findById(Integer id);

    List<Product> findByIds(List<Integer> ids);

    Product findByReference(String reference);

    List<Product> findProductsByReference(String reference);

    List<Product> findProductsByName(String name);

    List<Product> searchProductsByNameOrReference(String searchTerm, String searchType);

    @Transactional
    Product save(ProductDto productDto);

    @Transactional
    Product update(ProductDto productDto);

    @Transactional
    void updateAllProducts(Collection<Product> products);

    @Transactional
    void delete(Integer id);

    List<String> validateProducts(List<Product> products);

    void validateAndThrow(Product product);

    Product convertDtoToProduct(ProductDto productDto);

    List<Product> convertDtoToProduct(List<ProductDto> productDtos);

    ProductDto convertProductToDto(Product product);

    List<ProductDto> convertProductToDto(List<Product> products);

}
