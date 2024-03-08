package com.bs.dbperformancemetrics.controller.mongoDB;

import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.service.databse.mongoDB.mongo.MongoDBUserMongoServiceImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mongodb")
public class MongoDBUserController {
    private final MongoDBUserMongoServiceImp userService;

    public MongoDBUserController(MongoDBUserMongoServiceImp userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<MongoDBUser> getAllUsers() {
        return userService.findAll();
    }

}
