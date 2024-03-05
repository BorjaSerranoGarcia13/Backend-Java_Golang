package com.bs.dbperformancemetrics.service.performance.crud.update;

public interface IPerformanceUpdateService {

    String updateAllUsers();

    String updateUserById();

    String updateUserByIndexedField();

    String updateUserByNonIndexedField();

    String compareUpsertUpdate();

}
