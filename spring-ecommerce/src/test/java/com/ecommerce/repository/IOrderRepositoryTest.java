package com.ecommerce.repository;

import com.ecommerce.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IOrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IOrderRepository orderRepository;

    @Test
    public void testFindByReference() {
        Order order = new Order();
        order.setReference("D.testReference");
        entityManager.persist(order);
        entityManager.flush();

        Optional<Order> found = orderRepository.findByReference(order.getReference());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getReference()).isEqualTo(order.getReference());
    }

    @Test
    public void testFindAllByUserId() {
        Order order1 = new Order();
        order1.setUserId(1);
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setUserId(1);
        entityManager.persist(order2);

        entityManager.flush();

        List<Order> found = orderRepository.findAllByUserId(1);

        assertThat(found).isNotEmpty();
        assertThat(found.size()).isEqualTo(2);
    }
}
