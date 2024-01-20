package com.ecommerce.repository;

import com.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IProductRepository productRepository;

    @Test
    public void testFindProductsByName() {
        Product product = new Product();
        product.setName("testName");
        entityManager.persist(product);
        entityManager.flush();

        List<Product> found = productRepository.findProductsByName(product.getName());

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getName()).isEqualTo(product.getName());
    }

    @Test
    public void testFindByName() {
        Product product = new Product();
        product.setName("testName");
        entityManager.persist(product);
        entityManager.flush();

        Optional<Product> found = productRepository.findByName(product.getName());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getName()).isEqualTo(product.getName());
    }

    @Test
    public void testFindByReference() {
        Product product = new Product();
        product.setReference("P.testReference");
        entityManager.persist(product);
        entityManager.flush();

        Optional<Product> found = productRepository.findByReference(product.getReference());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getReference()).isEqualTo(product.getReference());
    }

    @Test
    public void testFindByNameOrReference() {
        Product product = new Product();
        product.setName("testName");
        product.setReference("P.testReference");
        entityManager.persist(product);
        entityManager.flush();

        Optional<Product> found = productRepository.findByNameOrReference(product.getName(), product.getReference());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getName()).isEqualTo(product.getName());
        assertThat(found.get().getReference()).isEqualTo(product.getReference());
    }

    @Test
    public void testMarkAsDeleted() {
        Product product = new Product();
        product.setDeleted(true);
        entityManager.persist(product);
        entityManager.flush();

        productRepository.markAsDeleted(product.getId());

        Product found = entityManager.find(Product.class, product.getId());

        assertThat(found.getDeleted()).isTrue();
    }

    @Test
    public void testFindByIsDeletedFalse() {
        Product product = new Product();
        product.setDeleted(false);
        entityManager.persist(product);
        entityManager.flush();

        Page<Product> found = productRepository.findByIsDeletedFalse(PageRequest.of(0, 10));

        assertThat(found.getContent()).isNotEmpty();
        assertThat(found.getContent().get(0).getDeleted()).isFalse();
    }

    @Test
    public void testFindByIdOrName() {
        Product product = new Product();
        product.setName("testName");
        entityManager.persist(product);
        entityManager.flush();

        List<Product> found = productRepository.findByIdOrName(product.getId(), product.getName());

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getId()).isEqualTo(product.getId());
        assertThat(found.get(0).getName()).isEqualTo(product.getName());
    }
}
