package com.bs.dbperformancemetrics.controller.crud;

import com.bs.dbperformancemetrics.service.performance.crud.read.PerformanceReadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/performance")
public class PerformanceReadController {

    private final PerformanceReadService performanceReadService;

    public PerformanceReadController(PerformanceReadService performanceReadService) {
        this.performanceReadService = performanceReadService;
    }

    @GetMapping("/findAllUsers")
    public ResponseEntity<String> findAllUsers() {
        String result = performanceReadService.findAllUsers();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findUserByIndexedField")
    public ResponseEntity<String> findUserByIndexedField() {
        String result = performanceReadService.findUserByIndexedField();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findUserByNonIndexedField")
    public ResponseEntity<String> findUserByNonIndexedField() {
        String result = performanceReadService.findUserByNonIndexedField();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/findUserFieldByIndexedField")
    public ResponseEntity<String> findUserFieldByIndexedField() {
        String result = performanceReadService.findUserFieldByIndexedField();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/compareReadIndexAndNonIndex")
    public ResponseEntity<String> compareReadIndexAndNonIndex() {
        String result = performanceReadService.compareReadIndexAndNonIndex();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}