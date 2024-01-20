package com.ecommerce.repository;

import com.ecommerce.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class IUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IUserRepository userRepository;

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUsername("testUsername");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByUsername(user.getUsername());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    public void testFindByEmail() {
        User user = new User();
        user.setEmail("testEmail@test.com");
        entityManager.persist(user);
        entityManager.flush();

        Optional<User> found = userRepository.findByEmail(user.getEmail());

        assertThat(found.isPresent()).isTrue();
        assertThat(found.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void testFindByUsernameOrEmailOrPhoneNumber() {
        User user = new User();
        user.setUsername("testUsername");
        user.setEmail("testEmail@test.com");
        user.setPhoneNumber("1234567890");
        entityManager.persist(user);
        entityManager.flush();

        List<User> found = userRepository.findByUsernameOrEmailOrPhoneNumber(user.getUsername(), user.getEmail(), user.getPhoneNumber());

        assertThat(found).isNotEmpty();
        assertThat(found.get(0).getUsername()).isEqualTo(user.getUsername());
        assertThat(found.get(0).getEmail()).isEqualTo(user.getEmail());
        assertThat(found.get(0).getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }
}
