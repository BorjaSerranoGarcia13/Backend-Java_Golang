package com.bs.dbperformancemetrics.service.databse.oracle.jpa;

import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.repository.oracle.jpa.OracleUserJPARepository;
import com.bs.dbperformancemetrics.service.IUserService;
import com.bs.dbperformancemetrics.utils.UserValidation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OracleUserJPAServiceImp implements IUserService<OracleUser, Long> {
    private final OracleUserJPARepository repository;

    @PersistenceContext
    private final EntityManager entityManager;

    public OracleUserJPAServiceImp(OracleUserJPARepository repository, EntityManager entityManager) {
        this.repository = repository;
        this.entityManager = entityManager;
    }

    // CREATE

    @Override
    @Transactional
    public void insert(OracleUser user) {
        UserValidation.validateUserCreation(user);
        entityManager.persist(createCopyOfUser(user));
    }

    @Override
    @Transactional
    public void insertAll(List<OracleUser> users) {
        UserValidation.validateUsersCreation(users);
        int batchSize = 1000;
        int i = 0;

        for (OracleUser user : users) {
            entityManager.persist(createCopyOfUser(user));
            i++;
            if (i % batchSize == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
        if (i % batchSize != 0) {
            entityManager.flush();
            entityManager.clear();
        }
    }

    @Override
    @Transactional
    public void save(OracleUser user) {
        UserValidation.validateUserCreation(user);
        repository.save(createCopyOfUser(user));
    }

    @Override
    @Transactional
    public void saveAll(List<OracleUser> users) {
        UserValidation.validateUsersCreation(users);
        repository.saveAll(createCopyOfUserList(users));
        entityManager.clear();
    }

    // READ

    @Override
    public OracleUser findById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to zero");
        }
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<OracleUser> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public OracleUser findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        return repository.findByUsername(username).orElse(null);
    }

    @Override
    public List<OracleUser> findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        List<OracleUser> users = repository.findByName(name);
        return users.isEmpty() ? null : users;
    }

    @Override
    public String findPasswordByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        return repository.findPasswordByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));
    }

    // UPDATE

    @Override
    @Transactional
    public void updateAll(List<OracleUser> users) {
        UserValidation.validateUsers(users);
        saveAll(createCopyOfUserList(users));
    }

    @Override
    @Transactional
    public void update(OracleUser user) {
        UserValidation.validateUser(user);

        repository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + user.getId() + " not found"));

        save(user);
    }

    @Override
    @Transactional
    public void updatePasswordByUsername(String username, String newPassword) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("User username cannot be null or empty");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }

        OracleUser user = repository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));

        user.setPassword(newPassword);

        UserValidation.validateUser(user);

        repository.save(user);
    }

    @Override
    @Transactional
    public void updatePasswordByName(String name, String newPassword) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }

        List<OracleUser> users = repository.findByName(name);

        if (users.isEmpty()) {
            throw new IllegalArgumentException("User with name " + name + " not found");
        }

        users.forEach(user -> user.setPassword(newPassword));

        UserValidation.validateUsers(users);

        repository.saveAll(users);
    }

    @Override
    @Transactional
    public void addFriend(Long userId, Long friendId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to zero");
        }
        if (friendId == null || friendId <= 0) {
            throw new IllegalArgumentException("Friend ID cannot be null or less than or equal to zero");
        }
        if (userId.equals(friendId)) {
            throw new IllegalArgumentException("User ID and friend ID cannot be the same");
        }

        OracleUser user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        user.addFriendId(friendId);

        repository.save(user);
    }

    @Override
    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        OracleUser user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        user.removeFriendId(friendId);

        repository.save(user);
    }

    // DELETE

    @Override
    @Transactional
    public void deleteAll() {
        repository.deleteAllInBatch();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to zero");
        }

        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("User username cannot be null or empty");
        }

        repository.deleteByUsername(username);
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }

        repository.deleteByName(name);
    }

    @Override
    public OracleUser createCopyOfUser(OracleUser originalUser) {
        return new OracleUser(originalUser);
    }

    @Override
    public List<OracleUser> createCopyOfUserList(List<OracleUser> originalList) {
        return originalList.parallelStream()
                .map(OracleUser::new)
                .toList();
    }

}
