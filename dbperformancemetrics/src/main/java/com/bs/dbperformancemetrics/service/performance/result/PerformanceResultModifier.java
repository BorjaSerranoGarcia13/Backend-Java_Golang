package com.bs.dbperformancemetrics.service.performance.result;

import com.bs.dbperformancemetrics.utils.FormatterUtils;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PerformanceResultModifier {

    public PerformanceResult generatePerformanceResultString(String databaseDetails, Long duration) {
        return new PerformanceResult(String.format("| %-30s | %-15s |", databaseDetails, duration), duration, "");
    }

    public PerformanceResult generatePerformanceResultString(String databaseDetails, String comparisonField, Long duration) {
        return new PerformanceResult(String.format("| %-30s | %-15s | %-15s |", databaseDetails, comparisonField, duration), duration, comparisonField);
    }

    public PerformanceResult modifyPerformanceResult(PerformanceResult performanceResult) {
        performanceResult.setDatabaseDetails(FormatterUtils.createEmptyRow() + performanceResult.getDatabaseDetails());
        return performanceResult;
    }

    public List<PerformanceResult> modifyPerformanceResults(List<PerformanceResult> performanceResult) {
        performanceResult.get(0).setDatabaseDetails(FormatterUtils.createEmptyRow() + performanceResult.get(0).getDatabaseDetails());
        return performanceResult;
    }
}
