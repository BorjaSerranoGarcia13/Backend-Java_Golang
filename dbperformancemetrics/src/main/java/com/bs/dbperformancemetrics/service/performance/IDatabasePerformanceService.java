package com.bs.dbperformancemetrics.service.performance;

import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;

import java.util.List;

public interface IDatabasePerformanceService {

    String getDatabaseDetails();

    void prepareEmptyDatabase();

    void resetDatabase();

    void seedDatabase();

    void initializeDatabase();

    PerformanceResult saveUsers(int numberOfData);

    PerformanceResult saveUser();

    List<PerformanceResult> compareUpsertInsert(int numberOfData);

    PerformanceResult findAllUsers();

    List<PerformanceResult> findUserByUsername();

    List<PerformanceResult> findUserByName();

    List<PerformanceResult> findPasswordByUsername();

    List<PerformanceResult> compareReadIndexAndNonIndex();

    PerformanceResult updateAllUsers();

    PerformanceResult deleteAllUsers();

}
