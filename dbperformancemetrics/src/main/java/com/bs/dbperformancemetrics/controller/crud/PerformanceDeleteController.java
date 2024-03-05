package com.bs.dbperformancemetrics.controller.crud;

import com.bs.dbperformancemetrics.service.performance.crud.delete.PerformanceDeleteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/performance")
public class PerformanceDeleteController {

    private final PerformanceDeleteService performanceDeleteService;

    public PerformanceDeleteController(PerformanceDeleteService performanceDeleteService) {
        this.performanceDeleteService = performanceDeleteService;
    }

    @DeleteMapping("/deleteAllUsers")
    public ResponseEntity<String> deleteAllUsers() {
        String result = performanceDeleteService.deleteAllUsers();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserById")
    public ResponseEntity<String> deleteUserById() {
        String result = performanceDeleteService.deleteUserById();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserByIndexedField")
    public ResponseEntity<String> deleteUserByIndexedField() {
        String result = performanceDeleteService.deleteUserByIndexedField();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/deleteUserByNonIndexedField")
    public ResponseEntity<String> deleteUserByNonIndexedField() {
        String result = performanceDeleteService.deleteUserByNonIndexedField();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/compareDeleteIndexAndNonIndex")
    public ResponseEntity<String> compareDeleteIndexAndNonIndex() {
        String result = performanceDeleteService.compareDeleteIndexAndNonIndex();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}