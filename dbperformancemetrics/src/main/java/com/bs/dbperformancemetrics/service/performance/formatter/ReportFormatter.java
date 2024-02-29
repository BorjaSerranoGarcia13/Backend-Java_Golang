package com.bs.dbperformancemetrics.service.performance.formatter;

import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResultGroup;
import com.bs.dbperformancemetrics.utils.FormatterUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportFormatter {

    public String formatDatabasePerformanceDetails(List<PerformanceResult> databaseDetailsAndExecutionTimes) {
        if (databaseDetailsAndExecutionTimes == null) {
            throw new IllegalArgumentException("Database details and execution times cannot be null");
        }

        return databaseDetailsAndExecutionTimes.stream()
                .map(pair -> String.format("%s", pair.getDatabaseDetails()))
                .collect(Collectors.joining("\n"));
    }

    public String formatPerformanceResultsForAllGroups(List<PerformanceResultGroup> performanceResultGroups) {
        if (performanceResultGroups == null) {
            throw new IllegalArgumentException("Performance result groups cannot be null");
        }

        return performanceResultGroups.stream()
                .map(group -> formatDatabasePerformanceDetails(group.getPerformanceResults()))
                .collect(Collectors.joining("\n"));
    }

    public String findBestPerformingDatabase(PerformanceResult bestPerformance) {
        if (bestPerformance != null) {
            String databaseDetails = bestPerformance.getDatabaseDetails().replace("|", "")
                    .replace("-", "").replace("\n", "")
                    .replaceAll(" +", " ").replaceAll("\\d", "");
            return "- Best Performing Database:" + databaseDetails +
                   "with an average execution time of " +
                   bestPerformance.getExecutionTime() +
                   " ns\n";
        } else {
            return "No data available to determine the Best performing database.";
        }
    }

    public String findWorstPerformingDatabase(PerformanceResult worstPerformance) {
        if (worstPerformance != null) {
            String databaseDetails = worstPerformance.getDatabaseDetails().replace("|", "")
                    .replace("-", "").replace("\n", "")
                    .replaceAll(" +", " ").replaceAll("\\d", "");
            return "- Least Performing Database:" + databaseDetails +
                   "with an average execution time of " +
                   worstPerformance.getExecutionTime() +
                   " ns\n";
        } else {
            return "No data available to determine the worst performing database.";
        }
    }

    public String findImprovementPercentage(Pair<PerformanceResult, PerformanceResult> bestAndWorstPerformances) {
        if (bestAndWorstPerformances != null) {
            double improvement = (double) (bestAndWorstPerformances.getSecond().getExecutionTime() - bestAndWorstPerformances.getFirst().getExecutionTime()) /
                                 bestAndWorstPerformances.getSecond().getExecutionTime() * 100;
            return String.format("- Improvement Over Least Efficient: %.2f%%\n", improvement);
        } else {
            return "No data available to determine the performance improvement.";
        }
    }

    public String findBestAndWorstPerformanceResultsByPosition(Map<String, Pair<PerformanceResult, PerformanceResult>> performancesByPosition) {
        if (performancesByPosition == null) {
            throw new IllegalArgumentException("Performances by position cannot be null");
        }

        return performancesByPosition.entrySet().parallelStream()
                .map(entry -> {
                    return String.format("%s\n%s%s\n", entry.getKey().toUpperCase(),
                            findBestPerformingDatabase(entry.getValue().getFirst()), findWorstPerformingDatabase(entry.getValue().getSecond()));
                })
                .collect(Collectors.joining());
    }

    public String findImprovementPercentage(Map<String, Pair<PerformanceResult, PerformanceResult>> performancesByPosition) {
        if (performancesByPosition == null) {
            throw new IllegalArgumentException("Performances by position cannot be null");
        }

        List<String> improvements = performancesByPosition.entrySet().parallelStream().map(entry -> {
            String improvement = findImprovementPercentage(entry.getValue());
            return String.format("%s\n%s", entry.getKey().toUpperCase(), improvement);
        }).collect(Collectors.toList());

        return String.join("\n", improvements);
    }

    public String formatReport(String functionDescription, int iterations, int elementCount, String formattedPerformanceResults,
                               String resultPerformingDatabase, String improvementPercentage) {
        if (functionDescription == null || formattedPerformanceResults == null || resultPerformingDatabase == null ||
            improvementPercentage == null) {
            throw new IllegalArgumentException("Function description, formatted performance results, result performing database, and improvement percentage cannot be null");
        }

        String formatString =
                """
                        =============================
                         PERFORMANCE ANALYSIS REPORT
                        =============================
                                
                        - Operation: %s
                        - Total Iterations: %d
                        - Total Elements Processed: %d
                                
                                
                        EXECUTION TIME (IN NANOSECONDS):
                        --------------------------------
                                        
                        |     DATABASE     |  IMPLEMENTATION  |    OPERATION    |  AVG TIME (ns)  |
                        %s
                        %s
                                                   
                        PERFORMANCE SUMMARY:
                        --------------------
                                        
                        %s
                        PERFORMANCE IMPROVEMENT:
                        ------------------------
                                        
                        %s
                                                
                        NOTE
                        ----
                        The performance improvement is calculated as the percentage reduction in execution time from the least efficient database to the best performing one.
                        """;

        return String.format(formatString,
                functionDescription, iterations, elementCount,
                formattedPerformanceResults, FormatterUtils.createEmptyRow(),
                resultPerformingDatabase,
                improvementPercentage);
    }
}
