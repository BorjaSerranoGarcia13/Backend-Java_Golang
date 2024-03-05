package com.bs.dbperformancemetrics.controller.crud;

import com.bs.dbperformancemetrics.service.performance.crud.update.PerformanceUpdateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/performance")
public class PerformanceUpdateController {

    private final PerformanceUpdateService performanceUpdateService;

    public PerformanceUpdateController(PerformanceUpdateService performanceUpdateService) {
        this.performanceUpdateService = performanceUpdateService;
    }

    @PutMapping("/updateAllUsers")
    public ResponseEntity<String> updateAllUsers() {
        String result = performanceUpdateService.updateAllUsers();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/updateUserById")
    public ResponseEntity<String> updateUserById() {
        String result = performanceUpdateService.updateUserById();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/updateUserByIndexedField")
    public ResponseEntity<String> updateUserByIndexedField() {
        String result = performanceUpdateService.updateUserByIndexedField();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/updateUserByNonIndexedField")
    public ResponseEntity<String> updateUserByNonIndexedField() {
        String result = performanceUpdateService.updateUserByNonIndexedField();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/compareUpsertUpdate")
    public ResponseEntity<String> compareUpsertUpdate() {
        String result = performanceUpdateService.compareUpsertUpdate();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}