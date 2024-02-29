package com.bs.dbperformancemetrics.service.mongoDB.mongo;

import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.repository.mongodb.mongo.MongoDBUserMongoRepository;
import com.bs.dbperformancemetrics.service.IUserService;
import com.bs.dbperformancemetrics.utils.UserValidation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MongoDBUserMongoServiceImp implements IUserService<MongoDBUser, String> {

    private final MongoDBUserMongoRepository repository;

    public MongoDBUserMongoServiceImp(MongoDBUserMongoRepository repository) {
        this.repository = repository;
    }

    // CREATE

    @Override
    @Transactional
    public void insert(MongoDBUser user) {
        UserValidation.validateUserCreation(user);
        repository.insert(user);
    }

    @Override
    @Transactional
    public void insertAll(List<MongoDBUser> users) {
        UserValidation.validateUsersCreation(users);
        for (MongoDBUser user : users) {
            insert(user);
        }
    }

    @Override
    @Transactional
    public void save(MongoDBUser user) {
        UserValidation.validateUserCreation(user);
        repository.save(user);
    }

    @Override
    @Transactional
    public void saveAll(List<MongoDBUser> users) {
        UserValidation.validateUsersCreation(users);
        repository.saveAll(users);
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

        return repository.findById(id).orElse(null);
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
    public void update(MongoDBUser user) {
        save(user);
    }

    @Override
    @Transactional
    public void updateAll(List<MongoDBUser> users) {
        saveAll(users);
    }

    // DELETE

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
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    @Transactional
    public void changeUserName(String id, String newName) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (newName == null || newName.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        MongoDBUser user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

        user.setName(newName);

        repository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String id, String newPassword) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        if (newPassword == null || newPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        MongoDBUser user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User with id " + id + " not found"));

        user.setPassword(newPassword);

        repository.save(user);
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

}
