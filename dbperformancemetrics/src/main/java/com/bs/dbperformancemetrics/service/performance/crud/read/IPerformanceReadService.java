package com.bs.dbperformancemetrics.service.performance.crud.read;

public interface IPerformanceReadService {

    String findAllUsers();

    String findUserByIndexedField();

    String findUserByNonIndexedField();

    String findUserFieldByIndexedField();

    String compareReadIndexAndNonIndex();

}
