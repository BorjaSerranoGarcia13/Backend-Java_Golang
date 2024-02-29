package com.bs.dbperformancemetrics.service.mongoDB.mongo;

import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.service.mongoDB.MongoDBUserUploader;
import com.bs.dbperformancemetrics.service.performance.IDatabasePerformanceService;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResultModifier;
import com.bs.dbperformancemetrics.utils.Constants;
import com.bs.dbperformancemetrics.utils.PerformanceMeasurementUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Supplier;

import static com.bs.dbperformancemetrics.utils.PerformanceMeasurementUtils.measureExecutionTime;

@Service
public class MongoDBMongoPerformanceService implements IDatabasePerformanceService {

    private final MongoDBUserUploader userUploader;

    private final MongoDBUserMongoServiceImp userService;

    private final PerformanceResultModifier performanceResultModifier;

    public MongoDBMongoPerformanceService(MongoDBUserUploader userUploader, MongoDBUserMongoServiceImp userService,
                                          PerformanceResultModifier performanceResultModifier) {
        this.userUploader = userUploader;
        this.userService = userService;
        this.performanceResultModifier = performanceResultModifier;
    }

    @Override
    public String getDatabaseDetails() {
        String databaseName = "MongoDB";
        String databaseImplementation = "Mongo";

        return String.format(" %-15s | %-15s ", databaseName, databaseImplementation);
    }

    @Override
    public void prepareEmptyDatabase() {
        userService.deleteAll();
    }

    @Override
    public void resetDatabase() {
        // No need to reset sequence
    }

    @Override
    public void seedDatabase() {
        final int startUserId = 1;
        List<MongoDBUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA, startUserId);
        userService.saveAll(users);
    }

    @Override
    public void initializeDatabase() {
        prepareEmptyDatabase();
        seedDatabase();
    }

    @Override
    public PerformanceResult saveUsers(int numberOfData) {
        final int startUserId = 1;
        List<MongoDBUser> users = userUploader.generateRandomUsers(numberOfData, startUserId);

        final long executionTime = measureExecutionTime(() -> {
            prepareEmptyDatabase();
            resetDatabase();
        }, () -> {
            userService.saveAll(users);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public PerformanceResult saveUser() {
        final int startUserId = 1;
        final int endUserId = Constants.NUMBER_OF_DATA + 1;

        List<MongoDBUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA, startUserId);
        MongoDBUser addUser = new MongoDBUser("user" + endUserId, "username" + endUserId, "password" + endUserId);

        final long executionTime = measureExecutionTime(() -> {
            prepareEmptyDatabase();
            resetDatabase();
            userService.saveAll(users);
        }, () -> {
            userService.save(addUser);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public List<PerformanceResult> compareUpsertInsert(int numberOfData) {
        final int startUserId = 1;
        List<MongoDBUser> users = userUploader.generateRandomUsers(numberOfData, startUserId);

        final long executionTimeUpsert = measureExecutionTime(() -> {
            prepareEmptyDatabase();
            resetDatabase();
        }, () -> {
            userService.saveAll(users);
        });

        final long executionTimeInsert = measureExecutionTime(() -> {
            prepareEmptyDatabase();
            resetDatabase();
        }, () -> {
            userService.insertAll(users);
        });

        return performanceResultModifier.modifyPerformanceResults(List.of(
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTimeUpsert),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Insert", executionTimeInsert)));
    }

    @Override
    public PerformanceResult findAllUsers() {
        initializeDatabase();

        final long executionTime = PerformanceMeasurementUtils.measureExecutionTime(userService::findAll);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find", executionTime));
    }

    @Override
    public List<PerformanceResult> findUserByUsername() {
        initializeDatabase();

        final int initPosition = 1;
        final int middlePosition = Constants.NUMBER_OF_DATA / 2;

        final long executionTimeBeginning = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findByUsername("username" + initPosition);
        });

        final long executionTimeMiddle = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findByUsername("username" + middlePosition);
        });

        final long executionTimeEnd = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findByUsername("username" + Constants.NUMBER_OF_DATA);
        });

        return performanceResultModifier.modifyPerformanceResults(List.of(
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First idx", executionTimeBeginning),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle idx", executionTimeMiddle),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last idx", executionTimeEnd)));
    }

    @Override
    public List<PerformanceResult> findUserByName() {
        initializeDatabase();

        final int initPosition = 1;
        final int middlePosition = Constants.NUMBER_OF_DATA / 2;

        final long executionTimeBeginning = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findByName("username" + initPosition);
        });

        final long executionTimeMiddle = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findByName("username" + middlePosition);
        });

        final long executionTimeEnd = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findByName("username" + Constants.NUMBER_OF_DATA);
        });

        return performanceResultModifier.modifyPerformanceResults(List.of(
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First", executionTimeBeginning),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle", executionTimeMiddle),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last", executionTimeEnd)));
    }

    @Override
    public List<PerformanceResult> findPasswordByUsername() {
        initializeDatabase();

        final int initPosition = 1;
        final int middlePosition = Constants.NUMBER_OF_DATA / 2;

        final long executionTimeBeginning = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findPasswordByUsername("username" + initPosition);
        });

        final long executionTimeMiddle = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findPasswordByUsername("username" + middlePosition);
        });

        final long executionTimeEnd = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            userService.findPasswordByUsername("username" + Constants.NUMBER_OF_DATA);
        });

        return performanceResultModifier.modifyPerformanceResults(List.of(
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First", executionTimeBeginning),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle", executionTimeMiddle),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last", executionTimeEnd)));
    }

    @Override
    public List<PerformanceResult> compareReadIndexAndNonIndex() {
        initializeDatabase();

        final int initPosition = 1;
        final int middlePosition = Constants.NUMBER_OF_DATA / 2;

        List<String> usernames = List.of("username" + initPosition, "username" + middlePosition, "username" + Constants.NUMBER_OF_DATA);

        List<Pair<Long, Long>> executionTimes = usernames.parallelStream().map(s -> {
                    long executionTimeByName = measureExecutionTime(() -> userService.findByName(s));
                    long executionTimeByUsername = measureExecutionTime(() -> userService.findByUsername(s));
                    return Pair.of(executionTimeByName, executionTimeByUsername);
                })
                .toList();

        final long averageExecutionTimeByName = executionTimes.stream().mapToLong(Pair::getFirst).sum() / usernames.size();
        final long averageExecutionTimeByUsername = executionTimes.stream().mapToLong(Pair::getSecond).sum() / usernames.size();

        return performanceResultModifier.modifyPerformanceResults(List.of(
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find By Non-Idx", averageExecutionTimeByName),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find By Idx", averageExecutionTimeByUsername)));
    }

    @Override
    public PerformanceResult updateAllUsers() {
        Supplier<List<MongoDBUser>> supplier = () -> {
            initializeDatabase();
            List<MongoDBUser> users = userService.findAll();
            users.forEach(user -> user.setName("Updated"));
            return users;
        };

        final long executionTime = measureExecutionTime(supplier, userService::saveAll);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Update", executionTime));
    }

    @Override
    public PerformanceResult deleteAllUsers() {
        final long executionTime = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            initializeDatabase();
            userService.deleteAll();
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete", executionTime));
    }

}
