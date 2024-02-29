package com.bs.dbperformancemetrics.service.performance.result;

public class PerformanceResult {
    private String databaseDetails;
    private Long executionTime;
    private String comparisonField;

    public PerformanceResult(String databaseDetails, Long executionTime, String comparisonField) {
        this.databaseDetails = databaseDetails;
        this.executionTime = executionTime;
        this.comparisonField = comparisonField;
    }

    public String getDatabaseDetails() {
        return databaseDetails;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public String getComparisonField() {
        return comparisonField;
    }

    public void setDatabaseDetails(String databaseDetails) {
        if (databaseDetails == null) {
            throw new IllegalArgumentException("Database details cannot be null");
        }
        this.databaseDetails = databaseDetails;
    }

    public void setExecutionTime(Long executionTime) {
        if (executionTime == null) {
            throw new IllegalArgumentException("Duration cannot be null");
        }
        this.executionTime = executionTime;
    }

    public void setComparisonField(String comparisonField) {
        if (comparisonField == null) {
            throw new IllegalArgumentException("Comparison cannot be null");
        }
        this.comparisonField = comparisonField;
    }
}
