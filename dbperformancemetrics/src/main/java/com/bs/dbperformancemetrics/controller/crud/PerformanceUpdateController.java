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

    @PutMapping("/updateAllUsersEmptyCollection")
    public ResponseEntity<String> updateAllUsersEmptyCollection() {
        String result = performanceUpdateService.updateAllUsersEmptyCollection();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/updateAllUsersFullCollection")
    public ResponseEntity<String> updateAllUsersFullCollection() {
        String result = performanceUpdateService.updateAllUsersFullCollection();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/updateUserFullCollection")
    public ResponseEntity<String> updateUserFullCollection() {
        String result = performanceUpdateService.updateUserFullCollection();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/compareUpdateMethods")
    public ResponseEntity<String> compareUpdateMethods() {
        String result = performanceUpdateService.compareUpdateMethods();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}