package com.bs.dbperformancemetrics.service.performance.measurement;

import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResultGroup;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PerformanceCalculator {

    public Pair<PerformanceResult, PerformanceResult> calculatePerformances(List<PerformanceResult> databaseDetailsAndExecutionTimes) {
        if (databaseDetailsAndExecutionTimes == null) {
            throw new IllegalArgumentException("Database details and execution times cannot be null");
        }

        PerformanceResult bestPerformance = databaseDetailsAndExecutionTimes.stream()
                .min(Comparator.comparing(PerformanceResult::getExecutionTime))
                .orElse(null);

        PerformanceResult worstPerformance = databaseDetailsAndExecutionTimes.stream()
                .max(Comparator.comparing(PerformanceResult::getExecutionTime))
                .orElse(null);

        if (bestPerformance == null || worstPerformance == null) {
            throw new IllegalStateException("Could not calculate performances: bestPerformance or worstPerformance is null");
        }

        return Pair.of(bestPerformance, worstPerformance);
    }

    public Map<String, Pair<PerformanceResult, PerformanceResult>> calculatePerformancesByPosition(List<PerformanceResultGroup> databaseDetailsAndExecutionTimes) {
        if (databaseDetailsAndExecutionTimes == null) {
            throw new IllegalArgumentException("Database details and execution times cannot be null");
        }

        return databaseDetailsAndExecutionTimes.stream()
                .flatMap(group -> group.getPerformanceResults().stream())
                .collect(Collectors.groupingBy(PerformanceResult::getComparisonField, LinkedHashMap::new, Collectors.toList()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> calculatePerformances(entry.getValue()), (a, b) -> b, LinkedHashMap::new));
    }
}
