package com.bs.dbperformancemetrics.service.performance.crud.create;

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
public class PerformanceCreateService implements IPerformanceCreateService {

    private final OracleJPAPerformanceService oracleJPAPerformanceService;
    private final OracleJDBCPerformanceService oracleJDBCPerformanceService;

    private final MongoDBMongoPerformanceService mongoDBMongoPerformanceService;
    private final MongoDBTemplatePerformanceService mongoDBTemplatePerformanceService;

    private final ResultFormatter resultFormatter;

    public PerformanceCreateService(OracleJPAPerformanceService oracleJPAPerformanceService,
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
    public String saveAllUsersEmptyCollection() {

        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.saveUsers(Constants.NUMBER_OF_DATA));

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.saveUsers(Constants.NUMBER_OF_DATA));

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.saveUsers(Constants.NUMBER_OF_DATA));

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.saveUsers(Constants.NUMBER_OF_DATA));

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a save operation to store a complete user record in a empty database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

    @Override
    public String saveUserFullCollection() {

        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();

        startTime = System.currentTimeMillis();

        startTime = System.currentTimeMillis();
        //databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.saveUser());
        endTime = System.currentTimeMillis();
        System.out.println("JPA TOTAL: " + (endTime - startTime) / 1000.0 + " segundos");

        startTime = System.currentTimeMillis();
        //databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.saveUser());
        endTime = System.currentTimeMillis();
        System.out.println("JDBC TOTAL: " + (endTime - startTime) / 1000.0 + " segundos");

        startTime = System.currentTimeMillis();
        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.saveUser());
        endTime = System.currentTimeMillis();
        System.out.println("mongo: " + (endTime - startTime) / 1000.0 + " segundos");

        startTime = System.currentTimeMillis();
        //databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.saveUser());
        endTime = System.currentTimeMillis();
        System.out.println("template: " + (endTime - startTime) / 1000.0 + " segundos");

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a save operation to store a single user record in a full database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA + 1);
    }

    @Override
    public String compareUpsertInsert() {

        List<IDatabasePerformanceService> services = List.of(
                oracleJPAPerformanceService,
                oracleJDBCPerformanceService,
                mongoDBMongoPerformanceService,
                mongoDBTemplatePerformanceService
        );

        List<PerformanceResultGroup> performanceResultGroups = services.stream()
                .map(service -> new PerformanceResultGroup(service.compareUpsertInsert()))
                .collect(Collectors.toList());

        return resultFormatter.formatForSpecificResultString(performanceResultGroups,
                "Performs a comparison of upsert versus insert operations for user saving in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }
}
