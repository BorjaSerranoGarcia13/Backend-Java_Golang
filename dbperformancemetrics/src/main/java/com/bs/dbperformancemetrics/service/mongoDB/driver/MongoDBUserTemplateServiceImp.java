package com.bs.dbperformancemetrics.service.mongoDB.driver;

import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.repository.mongodb.driver.MongoDBUserTemplateRepository;
import com.bs.dbperformancemetrics.service.IUserService;
import com.bs.dbperformancemetrics.utils.UserValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MongoDBUserTemplateServiceImp implements IUserService<MongoDBUser, String> {
    private final MongoDBUserTemplateRepository repository;

    public MongoDBUserTemplateServiceImp(MongoDBUserTemplateRepository repository) {
        this.repository = repository;
    }

    // CREATE

    @Override
    @Transactional
    public void insert(MongoDBUser user) {
        UserValidation.validateUserCreation(user);
        repository.insert(createCopyOfUser(user));
    }

    @Override
    @Transactional
    public void insertAll(List<MongoDBUser> users) {
        UserValidation.validateUsersCreation(users);
        repository.insertAll(createCopyOfUserList(users));
    }

    @Override
    @Transactional
    public void save(MongoDBUser user) {
        UserValidation.validateUserCreation(user);
        repository.save(createCopyOfUser(user));
    }

    @Override
    @Transactional
    public void saveAll(List<MongoDBUser> users) {
        UserValidation.validateUsersCreation(users);
        repository.saveAll(createCopyOfUserList(users));
    }

    // READ

    @Override
    public List<MongoDBUser> findAll() {
        return repository.findAll();
    }

    @Override
    public MongoDBUser findById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }

        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

    }

    @Override
    public MongoDBUser findByUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        return repository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));
    }

    @Override
    public List<MongoDBUser> findByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        List<MongoDBUser> users = repository.findByName(name);
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
    public void updateAll(List<MongoDBUser> users) {
        UserValidation.validateUsers(users);
        repository.updateAll(createCopyOfUserList(users));
    }

    @Override
    @Transactional
    public void update(MongoDBUser user) {
        UserValidation.validateUser(user);

        MongoDBUser updatedUser = repository.findById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + user.getId() + " not found"));

        repository.update(updatedUser);
    }

    @Override
    @Transactional
    public void updatePasswordByUsername(String username, String newPassword) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("User name cannot be null or empty");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("New password cannot be null or empty");
        }

        MongoDBUser user = repository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User with username " + username + " not found"));

        user.setPassword(newPassword);

        UserValidation.validateUser(user);

        repository.update(user);
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

        List<MongoDBUser> users = repository.findByName(name);

        if (users.isEmpty()) {
            throw new IllegalArgumentException("User with name " + name + " not found");
        }

        users.forEach(user -> user.setPassword(newPassword));

        UserValidation.validateUsers(users);

        repository.updateAll(users);
    }

    // DELETE

    @Override
    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }
    
    @Override
    @Transactional
    public void deleteById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
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
    @Transactional
    public void addFriend(String id, String friendId) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (friendId == null || friendId.isEmpty()) {
            throw new IllegalArgumentException("Friend ID cannot be null or empty");
        }
        if (id.equals(friendId)) {
            throw new IllegalArgumentException("User cannot remove itself from the friends list");
        }

        MongoDBUser user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

        user.addFriend(friendId);

        repository.save(user);
    }

    @Override
    @Transactional
    public void removeFriend(String id, String friendId) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (friendId == null || friendId.isEmpty()) {
            throw new IllegalArgumentException("Friend ID cannot be null or empty");
        }
        if (id.equals(friendId)) {
            throw new IllegalArgumentException("User cannot remove itself from the friends list");
        }

        MongoDBUser user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

        user.removeFriend(friendId);

        repository.save(user);
    }

    @Override
    public MongoDBUser createCopyOfUser(MongoDBUser originalUser) {
        return new MongoDBUser(originalUser);
    }

    @Override
    public List<MongoDBUser> createCopyOfUserList(List<MongoDBUser> originalList) {
        return originalList.parallelStream()
                .map(MongoDBUser::new)
                .collect(Collectors.toList());
    }

}
