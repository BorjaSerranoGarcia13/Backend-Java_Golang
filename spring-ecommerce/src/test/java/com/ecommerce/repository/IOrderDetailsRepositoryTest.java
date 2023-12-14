package com.ecommerce.repository;
import com.ecommerce.model.OrderDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IOrderDetailsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IOrderDetailRepository orderDetailRepository;

    @BeforeEach
    public void setup() {
        OrderDetails orderDetails1 = new OrderDetails();
        orderDetails1.setReference("DOrderRef1");
        entityManager.persist(orderDetails1);

        OrderDetails orderDetails2 = new OrderDetails();
        orderDetails2.setReference("DOrderRef2");
        entityManager.persist(orderDetails2);

        entityManager.flush();
    }

    @Test
    public void testFindByReference() {
        List<OrderDetails> orderDetails = orderDetailRepository.findByReference("DOrderRef1");

        assertEquals(1, orderDetails.size());
        assertEquals("DOrderRef1", orderDetails.get(0).getReference());
    }
}
