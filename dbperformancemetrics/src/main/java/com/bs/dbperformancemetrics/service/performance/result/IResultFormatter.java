package com.bs.dbperformancemetrics.service.performance.result;

import java.util.List;

public interface IResultFormatter {
    String formatForAllResultString(List<PerformanceResult> databaseDetailsAndExecutionTimes,
                                    String functionDescription, int iterations, int elementCount);

    String formatForSpecificResultString(List<PerformanceResultGroup> databaseDetailsAndExecutionTimes,
                                         String functionDescription, int iterations, int elementCount);

}
