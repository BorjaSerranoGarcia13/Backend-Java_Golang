package com.bs.dbperformancemetrics.service.performance.result;

import com.bs.dbperformancemetrics.service.performance.formatter.ReportFormatter;
import com.bs.dbperformancemetrics.service.performance.measurement.PerformanceCalculator;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ResultFormatter implements IResultFormatter {

    private final PerformanceCalculator performanceCalculator;
    private final ReportFormatter reportFormatter;

    public ResultFormatter(PerformanceCalculator performanceCalculator, ReportFormatter reportFormatter) {
        this.performanceCalculator = performanceCalculator;
        this.reportFormatter = reportFormatter;
    }

    @Override
    public String formatForAllResultString(List<PerformanceResult> databaseDetailsAndExecutionTimes,
                                           String functionDescription, int iterations, int elementCount) {
        if (databaseDetailsAndExecutionTimes == null || databaseDetailsAndExecutionTimes.isEmpty()) {
            throw new IllegalArgumentException("Database details and execution times cannot be null or empty");
        }

        Pair<PerformanceResult, PerformanceResult> bestAndWorstPerformances =
                performanceCalculator.calculatePerformances(databaseDetailsAndExecutionTimes);

        String formattedPerformanceDetails = reportFormatter.formatDatabasePerformanceDetails(databaseDetailsAndExecutionTimes);

        String performanceSummary = reportFormatter.findBestPerformingDatabase(bestAndWorstPerformances.getFirst()) +
                                    reportFormatter.findWorstPerformingDatabase(bestAndWorstPerformances.getSecond());

        String performanceImprovement = reportFormatter.findImprovementPercentage(bestAndWorstPerformances);


        return reportFormatter.formatReport(functionDescription, iterations, elementCount,
                formattedPerformanceDetails, performanceSummary, performanceImprovement);
    }

    @Override
    public String formatForSpecificResultString(List<PerformanceResultGroup> databaseDetailsAndExecutionTimes,
                                                String functionDescription, int iterations, int elementCount) {
        if (databaseDetailsAndExecutionTimes == null || databaseDetailsAndExecutionTimes.isEmpty()) {
            throw new IllegalArgumentException("Database details and execution times cannot be null or empty");
        }

        Map<String, Pair<PerformanceResult, PerformanceResult>> bestAndWorstPerformances =
                performanceCalculator.calculatePerformancesByPosition(databaseDetailsAndExecutionTimes);

        String formattedPerformanceResults = reportFormatter.formatPerformanceResultsForAllGroups(databaseDetailsAndExecutionTimes);

        String performanceSummary = reportFormatter.findBestAndWorstPerformanceResultsByPosition(bestAndWorstPerformances);

        String performanceImprovement = reportFormatter.findImprovementPercentage(bestAndWorstPerformances);

        return reportFormatter.formatReport(functionDescription, iterations, elementCount,
                formattedPerformanceResults, performanceSummary, performanceImprovement);
    }

}
