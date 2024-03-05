package com.bs.dbperformancemetrics.service.performance.crud.delete;

public interface IPerformanceDeleteService {

    String deleteAllUsers();

    String deleteUserById();

    String deleteUserByIndexedField();

    String deleteUserByNonIndexedField();

    String compareDeleteIndexAndNonIndex();

}
