package com.bs.dbperformancemetrics.service.performance.crud.read;

import com.bs.dbperformancemetrics.service.mongoDB.driver.MongoDBTemplatePerformanceService;
import com.bs.dbperformancemetrics.service.mongoDB.mongo.MongoDBMongoPerformanceService;
import com.bs.dbperformancemetrics.service.oracle.jdbc.OracleJDBCPerformanceService;
import com.bs.dbperformancemetrics.service.oracle.jpa.OracleJPAPerformanceService;
import com.bs.dbperformancemetrics.service.performance.IDatabasePerformanceService;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResultGroup;
import com.bs.dbperformancemetrics.service.performance.result.ResultFormatter;
import com.bs.dbperformancemetrics.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerformanceReadService implements IPerformanceReadService {

    private final OracleJPAPerformanceService oracleJPAPerformanceService;
    private final OracleJDBCPerformanceService oracleJDBCPerformanceService;

    private final MongoDBMongoPerformanceService mongoDBMongoPerformanceService;
    private final MongoDBTemplatePerformanceService mongoDBTemplatePerformanceService;

    private final ResultFormatter resultFormatter;

    public PerformanceReadService(OracleJPAPerformanceService oracleJPAPerformanceService,
                                  OracleJDBCPerformanceService oracleJDBCPerformanceService,
                                  MongoDBMongoPerformanceService mongoDBMongoPerformanceService,
                                  MongoDBTemplatePerformanceService mongoDBTemplatePerformanceService,
                                  ResultFormatter resultFormatter) {
        this.oracleJPAPerformanceService = oracleJPAPerformanceService;
        this.oracleJDBCPerformanceService = oracleJDBCPerformanceService;

        this.mongoDBMongoPerformanceService = mongoDBMongoPerformanceService;
        this.mongoDBTemplatePerformanceService = mongoDBTemplatePerformanceService;

        this.resultFormatter = resultFormatter;
    }

    @Override
    public String findAllUsers() {

        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.findAllUsers());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.findAllUsers());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.findAllUsers());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.findAllUsers());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a read operation to retrieve all users record by a non-indexed field at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String findUserByIndexedField() {

        List<IDatabasePerformanceService> services = List.of(
                oracleJPAPerformanceService,
                oracleJDBCPerformanceService,
                mongoDBMongoPerformanceService,
                mongoDBTemplatePerformanceService
        );

        List<PerformanceResultGroup> performanceResultGroups = services.stream()
                .map(service -> new PerformanceResultGroup(service.findUserByUsername()))
                .collect(Collectors.toList());

        return resultFormatter.formatForSpecificResultString(performanceResultGroups,
                "Performs a read operation to retrieve a complete user record by a indexed field at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String findUserByNonIndexedField() {

        List<IDatabasePerformanceService> services = List.of(
                oracleJPAPerformanceService,
                oracleJDBCPerformanceService,
                mongoDBMongoPerformanceService,
                mongoDBTemplatePerformanceService
        );

        List<PerformanceResultGroup> performanceResultGroups = services.stream()
                .map(service -> new PerformanceResultGroup(service.findUserByName()))
                .collect(Collectors.toList());

        return resultFormatter.formatForSpecificResultString(performanceResultGroups,
                "Performs a read operation to retrieve a complete user record by a non-indexed field at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String findUserFieldByIndexedField() {

        List<IDatabasePerformanceService> services = List.of(
                oracleJPAPerformanceService,
                oracleJDBCPerformanceService,
                mongoDBMongoPerformanceService,
                mongoDBTemplatePerformanceService
        );

        List<PerformanceResultGroup> performanceResultGroups = services.stream()
                .map(service -> new PerformanceResultGroup(service.findPasswordByUsername()))
                .collect(Collectors.toList());

        return resultFormatter.formatForSpecificResultString(performanceResultGroups,
                "Performs a read operation to retrieve a specific field of a user record by an indexed field at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String compareReadIndexAndNonIndex() {
        List<IDatabasePerformanceService> services = List.of(
                oracleJPAPerformanceService,
                oracleJDBCPerformanceService,
                mongoDBMongoPerformanceService,
                mongoDBTemplatePerformanceService
        );

        List<PerformanceResultGroup> performanceResultGroups = services.stream()
                .map(service -> new PerformanceResultGroup(service.compareReadIndexAndNonIndex()))
                .collect(Collectors.toList());

        return resultFormatter.formatForSpecificResultString(performanceResultGroups,
                "Performs a comparison of read operations by indexed and non-indexed fields at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }
}
