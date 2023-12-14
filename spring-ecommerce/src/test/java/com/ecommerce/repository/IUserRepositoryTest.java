package com.ecommerce.repository;
import com.ecommerce.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IUserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IUserRepository userRepository;

    @BeforeEach
    public void setup() {
        User user1 = new User();
        user1.setName("name1");
        user1.setUsername("username1");
        user1.setPassword("password1");
        user1.setEmail("user1@example.com");
        user1.setAddress("address1");
        user1.setPhoneNumber("123");
        user1.setAdmin(true);
        entityManager.persist(user1);

        User user2 = new User();
        user2.setName("name2");
        user2.setUsername("username2");
        user2.setPassword("password2");
        user2.setEmail("user2@example.com");
        user2.setAddress("address2");
        user2.setPhoneNumber("1234");
        user2.setAdmin(false);
        entityManager.persist(user2);

        entityManager.flush();
    }

        @Test
    public void testFindByUsername() {
        Optional<User> user = userRepository.findByUsername("username1");

        assertTrue(user.isPresent());
        assertEquals("username1", user.get().getUsername());
    }

    @Test
    public void testFindByEmail() {
        Optional<User> user = userRepository.findByEmail("user1@example.com");

        assertTrue(user.isPresent());
        assertEquals("user1@example.com", user.get().getEmail());
    }

    @Test
    public void testFindByUsernameOrEmailOrPhoneNumber() {
        List<User> users = userRepository.findByUsernameOrEmailOrPhoneNumber("username1", "user1@example.com", null);

        assertEquals(1, users.size());
        assertEquals("username1", users.get(0).getUsername());
        assertEquals("user1@example.com", users.get(0).getEmail());
    }
}
