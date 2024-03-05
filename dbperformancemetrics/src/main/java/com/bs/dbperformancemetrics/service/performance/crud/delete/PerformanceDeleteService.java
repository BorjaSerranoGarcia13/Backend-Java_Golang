package com.bs.dbperformancemetrics.service.performance.crud.delete;

import com.bs.dbperformancemetrics.service.mongoDB.driver.MongoDBTemplatePerformanceService;
import com.bs.dbperformancemetrics.service.mongoDB.mongo.MongoDBMongoPerformanceService;
import com.bs.dbperformancemetrics.service.oracle.jdbc.OracleJDBCPerformanceService;
import com.bs.dbperformancemetrics.service.oracle.jpa.OracleJPAPerformanceService;
import com.bs.dbperformancemetrics.service.performance.IDatabasePerformanceService;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;
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

        long startTime = System.currentTimeMillis();
        long endTime = System.currentTimeMillis();

        startTime = System.currentTimeMillis();
        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.deleteUserById());
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo: " + (endTime - startTime) / 1000.0 + " segundos");

        startTime = System.currentTimeMillis();
        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.deleteUserById());
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo: " + (endTime - startTime) / 1000.0 + " segundos");

        startTime = System.currentTimeMillis();
        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.deleteUserById());
        endTime = System.currentTimeMillis();
        System.out.println("Tiempo: " + (endTime - startTime) / 1000.0 + " segundos");

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

        List<PerformanceResult> databaseDetailsAndExecutionTimes = new ArrayList<>();

        //databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.deleteUserByName());
        //databaseDetailsAndExecutionTimes.add(oracleJPAPerformanceService.deleteUserByUsername());

        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.deleteUserByName());
        databaseDetailsAndExecutionTimes.add(oracleJDBCPerformanceService.deleteUserByUsername());

        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.deleteUserByName());
        databaseDetailsAndExecutionTimes.add(mongoDBMongoPerformanceService.deleteUserByUsername());

        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.deleteUserByName());
        databaseDetailsAndExecutionTimes.add(mongoDBTemplatePerformanceService.deleteUserByUsername());

        return resultFormatter.formatForAllResultString(databaseDetailsAndExecutionTimes,
                "Performs a comparison of delete operations by indexed and non-indexed fields at different positions (first, middle, last) in the database, and calculates the average execution time.",
                Constants.NUMBER_OF_ITERATIONS, Constants.NUMBER_OF_DATA);
    }

}
