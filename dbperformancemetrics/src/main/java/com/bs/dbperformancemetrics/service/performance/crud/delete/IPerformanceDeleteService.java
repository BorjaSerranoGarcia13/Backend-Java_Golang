package com.bs.dbperformancemetrics.service.performance.crud.delete;

public interface IPerformanceDeleteService {

    String deleteAllUsers();

    String deleteUserByIndexedField();

    String deleteUserByNonIndexedField();

    String deleteUserFieldByIndexedField();

    // esto debe comparar delete por no indexed field y find by index y luego borrar por id.
    // que este por la mitad de la lista es ok
    String compareDeleteMethods();

}
