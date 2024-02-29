package com.bs.dbperformancemetrics.controller.crud;

import com.bs.dbperformancemetrics.service.performance.crud.create.PerformanceCreateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/performance")
public class PerformanceCreateController {
    private final PerformanceCreateService performanceCreateService;

    public PerformanceCreateController(PerformanceCreateService performanceCreateService) {
        this.performanceCreateService = performanceCreateService;
    }

    @PostMapping("/saveAllUsersEmptyCollection")
    public ResponseEntity<String> saveAllUsersEmptyCollection() {
        String result = performanceCreateService.saveAllUsersEmptyCollection();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/saveUserFullCollection")
    public ResponseEntity<String> saveUserFullCollection() {
        String result = performanceCreateService.saveUserFullCollection();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/compareUpsertInsert")
    public ResponseEntity<String> compareUpsertInsert() {
        String result = performanceCreateService.compareUpsertInsert();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}