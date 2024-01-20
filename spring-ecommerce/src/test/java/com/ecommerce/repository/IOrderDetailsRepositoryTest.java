package com.ecommerce.repository;

import com.ecommerce.model.OrderDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IOrderDetailsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IOrderDetailsRepository orderDetailsRepository;

    @Test
    public void testFindById() {
        OrderDetails orderDetails = new OrderDetails();
        entityManager.persist(orderDetails);
        entityManager.flush();

        Optional<OrderDetails> found = orderDetailsRepository.findById(orderDetails.getId());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getId()).isEqualTo(orderDetails.getId());
    }
}
