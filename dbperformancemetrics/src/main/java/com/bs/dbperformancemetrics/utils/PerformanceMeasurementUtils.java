package com.bs.dbperformancemetrics.utils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PerformanceMeasurementUtils {

    public static long measureExecutionTime(Runnable operation) {
        final long startTime = System.nanoTime();

        for (int i = 0; i < Constants.NUMBER_OF_ITERATIONS; i++) {
            operation.run();
        }
        final long endTime = System.nanoTime();

        return (endTime - startTime) / Constants.NUMBER_OF_ITERATIONS;
    }

    public static Long measureExecutionTime(Runnable additionalOperation, Runnable mainOperation) {
        long startTime;
        long endTime;
        long totalExecutionTime = 0;

        for (int i = 0; i < Constants.NUMBER_OF_ITERATIONS; i++) {
            additionalOperation.run();

            startTime = System.nanoTime();
            mainOperation.run();
            endTime = System.nanoTime();

            totalExecutionTime += (endTime - startTime);
        }

        return totalExecutionTime / Constants.NUMBER_OF_ITERATIONS;
    }

    public static <T> Long measureExecutionTime(Supplier<List<T>> supplierOperation,
                                                Consumer<List<T>> consumerOperation) {
        long startTime;
        long endTime;
        long totalExecutionTime = 0;
        List<T> supplierResult;

        for (int i = 0; i < Constants.NUMBER_OF_ITERATIONS; i++) {
            supplierResult = supplierOperation.get();

            startTime = System.nanoTime();
            consumerOperation.accept(supplierResult);
            endTime = System.nanoTime();

            totalExecutionTime += (endTime - startTime);
        }

        return totalExecutionTime / Constants.NUMBER_OF_ITERATIONS;
    }

}
