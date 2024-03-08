package com.bs.dbperformancemetrics.controller.oracle;

import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.service.databse.oracle.jdbc.OracleUserJDBCServiceImp;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/oracle")
public class OracleUserController {
    private final OracleUserJDBCServiceImp userService;
    public OracleUserController(OracleUserJDBCServiceImp oracleUserService) {
        this.userService = oracleUserService;
    }
    @GetMapping("/users")
    public List<OracleUser> getAllUsers() {
        return userService.findAll();
    }

}