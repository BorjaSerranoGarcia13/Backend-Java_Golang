package com.bs.dbperformancemetrics.service.performance.result;

import java.util.List;

public class PerformanceResultGroup {
    private List<PerformanceResult> performanceResults;

    public PerformanceResultGroup(List<PerformanceResult> performanceResults) {
        this.performanceResults = performanceResults;
    }

    public List<PerformanceResult> getPerformanceResults() {
        return performanceResults;
    }

    public void setPerformanceResults(List<PerformanceResult> performanceResults) {
        if (performanceResults == null) {
            throw new IllegalArgumentException("Performance results cannot be null");
        }
        this.performanceResults = performanceResults;
    }
}
