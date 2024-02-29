package com.bs.dbperformancemetrics.service.performance.crud.update;

public interface IPerformanceUpdateService {

    String updateAllUsersEmptyCollection();

    String updateAllUsersFullCollection();

    String updateUserFullCollection();

    // esto debe comparar entre update y upsert
    String compareUpdateMethods();
}
