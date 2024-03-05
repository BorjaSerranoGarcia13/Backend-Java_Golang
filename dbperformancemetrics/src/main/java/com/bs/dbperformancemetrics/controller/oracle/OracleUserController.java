package com.bs.dbperformancemetrics.controller.oracle;

import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.service.oracle.jpa.OracleUserJPAServiceImp;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/oracle")
public class OracleUserController {
    private final OracleUserJPAServiceImp oracleUserService;

    public OracleUserController(OracleUserJPAServiceImp oracleUserService) {
        this.oracleUserService = oracleUserService;
    }

    @PostMapping("/user")
    public void saveUser(@RequestBody OracleUser user) {
        oracleUserService.save(user);
    }

    @GetMapping("/user/{id}")
    public OracleUser getUserById(@PathVariable Long id) {
        return oracleUserService.findById(id);
    }

    @GetMapping("/users")
    public List<OracleUser> getAllUsers() {
        return oracleUserService.findAll();
    }

    @PutMapping("/user")
    public void updateUser(@RequestBody OracleUser user) {
        oracleUserService.save(user);
    }

    @DeleteMapping("/user/{id}")
    public void deleteUser(@PathVariable Long id) {
        oracleUserService.deleteById(id);
    }

    @PostMapping("/user/{userId}/friend")
    public void addFriend(@PathVariable Long userId, @RequestBody Long friendId) {
        oracleUserService.addFriend(userId, friendId);
    }

    @DeleteMapping("/user/{userId}/friend")
    public void removeFriend(@PathVariable Long userId, @RequestBody Long friendId) {
        oracleUserService.removeFriend(userId, friendId);
    }
}