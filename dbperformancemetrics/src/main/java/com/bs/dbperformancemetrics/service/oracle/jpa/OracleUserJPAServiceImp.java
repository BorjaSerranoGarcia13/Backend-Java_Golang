package com.bs.dbperformancemetrics.service.oracle.jpa;

import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.repository.oracle.jpa.OracleUserJPARepository;
import com.bs.dbperformancemetrics.service.IUserService;
import com.bs.dbperformancemetrics.utils.UserValidation;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OracleUserJPAServiceImp implements IUserService<OracleUser, Long> {
    private final OracleUserJPARepository repository;

    public OracleUserJPAServiceImp(OracleUserJPARepository repository) {
        this.repository = repository;
    }

    // CREATE

    @Override
    @Transactional
    public void insert(OracleUser user) {
        save(user);
    }

    @Override
    @Transactional
    public void insertAll(List<OracleUser> users) {
        saveAll(users);
    }

    @Override
    @Transactional
    public void save(OracleUser user) {
        UserValidation.validateUserCreation(user);
        repository.save(user);
    }

    @Override
    @Transactional
    public void saveAll(List<OracleUser> users) {
        UserValidation.validateUsersCreation(users);
        repository.saveAll(users);
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

        return repository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));
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
    public void update(OracleUser user) {
        save(user);
    }

    @Override
    @Transactional
    public void updateAll(List<OracleUser> users) {
        saveAll(users);
    }

    // DELETE

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
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    @Transactional
    public void changeUserName(Long userId, String newName) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to zero");
        }
        if (newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("New name cannot be null or empty");
        }

        OracleUser user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        user.setName(newName);

        repository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String newPassword) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID cannot be null or less than or equal to zero");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }

        OracleUser user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        user.setPassword(newPassword);

        repository.save(user);
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

        user.addFriend(friendId);

        repository.save(user);
    }

    @Override
    @Transactional
    public void removeFriend(Long userId, Long friendId) {
        OracleUser user = repository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + userId + " not found"));

        user.removeFriend(friendId);

        repository.save(user);
    }

}
