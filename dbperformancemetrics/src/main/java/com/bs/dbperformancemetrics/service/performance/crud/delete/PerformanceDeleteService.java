package com.bs.dbperformancemetrics.service.performance.crud.delete;

import com.bs.dbperformancemetrics.service.databse.mongoDB.driver.MongoDBTemplatePerformanceService;
import com.bs.dbperformancemetrics.service.databse.mongoDB.mongo.MongoDBMongoPerformanceService;
import com.bs.dbperformancemetrics.service.databse.oracle.jdbc.OracleJDBCPerformanceService;
import com.bs.dbperformancemetrics.service.databse.oracle.jpa.OracleJPAPerformanceService;
import com.bs.dbperformancemetrics.service.performance.IDatabasePerformanceService;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResultGroup;
import com.bs.dbperformancemetrics.service.performance.result.ResultFormatter;
import com.bs.dbperformancemetrics.utils.Constants;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PerformanceDeleteService implements IPerformanceDeleteService {

    private final OracleJPAPerformanceService oracleJPAPerformanceService;
    private final OracleJDBCPerformanceService oracleJDBCPerformanceService;

    private final MongoDBMongoPerformanceService mongoDBMongoPerformanceService;
    private final MongoDBTemplatePerformanceService mongoDBTemplatePerformanceService;

    private final ResultFormatter resultFormatter;

    public PerformanceDeleteService(OracleJPAPerformanceService oracleJPAPerformanceService,
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
    public String deleteAllUsers() {

        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.deleteAllUsers());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.deleteAllUsers());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.deleteAllUsers());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.deleteAllUsers());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a delete operation to remove a complete user record in a full database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String deleteUserById() {
        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.deleteUserById());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.deleteUserById());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.deleteUserById());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.deleteUserById());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a delete operation to remove a complete user record by ID at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String deleteUserByIndexedField() {
        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.deleteUserByUsername());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.deleteUserByUsername());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.deleteUserByUsername());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.deleteUserByUsername());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a delete operation to remove a complete user record by a indexed field at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String deleteUserByNonIndexedField() {
        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.deleteUserByName());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.deleteUserByName());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.deleteUserByName());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.deleteUserByName());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a delete operation to remove a complete user record by a non-indexed field at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String compareDeleteIndexAndNonIndex() {

        List<IDatabasePerformanceService> services = List.of(
                oracleJPAPerformanceService,
                oracleJDBCPerformanceService,
                mongoDBMongoPerformanceService,
                mongoDBTemplatePerformanceService
        );

        List<PerformanceResultGroup> performanceResultGroups = services.stream()
                .map(service -> new PerformanceResultGroup(service.compareDeleteIndexAndNonIndex()))
                .toList();

        return resultFormatter.formatForSpecificResultString(performanceResultGroups,
                "Performs a comparison of delete operations by indexed and non-indexed fields at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

}
