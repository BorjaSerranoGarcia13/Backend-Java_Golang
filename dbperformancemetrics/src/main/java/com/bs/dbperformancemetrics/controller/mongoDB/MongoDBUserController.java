package com.bs.dbperformancemetrics.controller.mongoDB;

import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.service.mongoDB.mongo.MongoDBUserMongoServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mongodb")
public class MongoDBUserController {
    private final MongoDBUserMongoServiceImp mongoDBUserService;

    public MongoDBUserController(MongoDBUserMongoServiceImp mongoDBUserService) {
        this.mongoDBUserService = mongoDBUserService;
    }

    @PostMapping("/user")
    public void saveUser(@RequestBody MongoDBUser user) {
        mongoDBUserService.save(user);
    }

    @GetMapping("/user/{id}")
    public void getUserById(@PathVariable String id) {
        mongoDBUserService.findById(id);
    }

    @GetMapping("/users")
    public List<MongoDBUser> getAllUsers() {
        return mongoDBUserService.findAll();
    }

    @PutMapping("/user/update")
    public void updateUser(@RequestBody MongoDBUser user) {
        mongoDBUserService.save(user);
    }

    @DeleteMapping("/user/delete/{id}")
    public void deleteUser(@PathVariable String id) {
        mongoDBUserService.deleteById(id);
    }

    @PatchMapping("/{userId}/name")
    public void changeUserName(@PathVariable String userId, @RequestBody String newName) {
        mongoDBUserService.changeUserName(userId, newName);
    }

    @PatchMapping("/{userId}/password")
    public void changePassword(@PathVariable String userId, @RequestBody String newName) {
        mongoDBUserService.changePassword(userId, newName);
    }

    @PostMapping("/{userId}/friend")
    public void addFriend(@PathVariable String userId, @RequestBody String friendId) {
        mongoDBUserService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friend")
    public void removeFriend(@PathVariable String userId, @RequestBody String friendId) {
        mongoDBUserService.removeFriend(userId, friendId);
    }


}
