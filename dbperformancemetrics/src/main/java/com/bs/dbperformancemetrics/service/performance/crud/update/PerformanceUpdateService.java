package com.bs.dbperformancemetrics.service.performance.crud.update;

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
public class PerformanceUpdateService implements IPerformanceUpdateService {

    private final OracleJPAPerformanceService oracleJPAPerformanceService;
    private final OracleJDBCPerformanceService oracleJDBCPerformanceService;

    private final MongoDBMongoPerformanceService mongoDBMongoPerformanceService;
    private final MongoDBTemplatePerformanceService mongoDBTemplatePerformanceService;

    private final ResultFormatter resultFormatter;

    public PerformanceUpdateService(OracleJPAPerformanceService oracleJPAPerformanceService,
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
    public String updateAllUsers() {

        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.updateAllUsers());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.updateAllUsers());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.updateAllUsers());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.updateAllUsers());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a update operation to store a complete user record, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);

    }

    @Override
    public String updateUserById() {
        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.updateUserById());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.updateUserById());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.updateUserById());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.updateUserById());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a update operation to store a single user record by ID at different positions (first, middle, last), and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA + 1);
    }

    @Override
    public String updateUserByIndexedField() {
        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.updateUserByIndexedField());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.updateUserByIndexedField());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.updateUserByIndexedField());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.updateUserByIndexedField());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a update operation to store a single user record by indexed field at different positions (first, middle, last), and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA + 1);
    }

    @Override
    public String updateUserByNonIndexedField() {
        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.updateUserByNonIndexedField());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.updateUserByNonIndexedField());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.updateUserByNonIndexedField());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.updateUserByNonIndexedField());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a update operation to store a single user record by non-indexed field at different positions (first, middle, last), and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA + 1);
    }

    @Override
    public String compareUpsertUpdate() {

        List<IDatabasePerformanceService> services = List.of(
                oracleJPAPerformanceService,
                oracleJDBCPerformanceService,
                mongoDBMongoPerformanceService,
                mongoDBTemplatePerformanceService
        );

        List<PerformanceResultGroup> performanceResultGroups = services.stream()
                .map(service -> new PerformanceResultGroup(service.compareUpsertUpdate()))
                .collect(Collectors.toList());

        return resultFormatter.formatForSpecificResultString(performanceResultGroups,
                "Performs a comparison of upsert versus insert operations for user updating in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

}
