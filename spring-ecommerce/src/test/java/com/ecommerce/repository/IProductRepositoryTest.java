package com.ecommerce.repository;
import com.ecommerce.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IProductRepository productRepository;

    @BeforeEach
    public void setup() {
        Product product1 = new Product();
        product1.setName("Product1");
        product1.setReference("ProductRef1");
        entityManager.persist(product1);

        Product product2 = new Product();
        product2.setName("Product2");
        product2.setReference("ProductRef2");
        entityManager.persist(product2);

        entityManager.flush();
    }

    @Test
    public void testFindProductsByName() {
        List<Product> products = productRepository.findProductsByName("Product1");

        assertEquals(1, products.size());
        assertEquals("Product1", products.get(0).getName());
    }

    @Test
    public void testFindByName() {
        Optional<Product> product = productRepository.findByName("Product1");

        assertTrue(product.isPresent());
        assertEquals("Product1", product.get().getName());
    }

    @Test
    public void testFindByReference() {
        Optional<Product> product = productRepository.findByReference("ProductRef1");

        assertTrue(product.isPresent());
        assertEquals("ProductRef1", product.get().getReference());
    }
}
