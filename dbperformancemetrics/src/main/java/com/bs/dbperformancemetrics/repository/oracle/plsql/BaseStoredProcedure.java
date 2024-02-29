package com.bs.dbperformancemetrics.repository.oracle.plsql;

import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;

public abstract class BaseStoredProcedure extends StoredProcedure {

    public BaseStoredProcedure(DataSource dataSource, String storedProcedureName) {
        super(dataSource, storedProcedureName);
        declareParameters();
        compile();
    }

    protected abstract void declareParameters();
}