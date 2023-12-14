package com.ecommerce.repository;
import com.ecommerce.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IOrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IOrderRepository orderRepository;

    @BeforeEach
    public void setup() {
        Order order1 = new Order();
        order1.setReference("DOrderRef1");
        entityManager.persist(order1);

        Order order2 = new Order();
        order2.setReference("DOrderRef2");
        entityManager.persist(order2);

        entityManager.flush();
    }

    @Test
    public void testFindByReference() {
        Optional<Order> order = orderRepository.findByReference("DOrderRef1");

        assertTrue(order.isPresent());
        assertEquals("DOrderRef1", order.get().getReference());
    }
}
