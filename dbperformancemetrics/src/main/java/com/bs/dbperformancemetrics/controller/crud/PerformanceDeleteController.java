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

    @DeleteMapping("/deleteUserFieldByIndexedField")
    public ResponseEntity<String> deleteUserFieldByIndexedField() {
        String result = performanceDeleteService.deleteUserFieldByIndexedField();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/compareDeleteMethods")
    public ResponseEntity<String> compareDeleteMethods() {
        String result = performanceDeleteService.compareDeleteMethods();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}