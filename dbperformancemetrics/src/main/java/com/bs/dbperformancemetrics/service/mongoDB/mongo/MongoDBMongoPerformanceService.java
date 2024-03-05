package com.bs.dbperformancemetrics.service.mongoDB.mongo;

import com.bs.dbperformancemetrics.config.MongoDBConfig;
import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.service.mongoDB.MongoDBUserUploader;
import com.bs.dbperformancemetrics.service.performance.IDatabasePerformanceService;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResultModifier;
import com.bs.dbperformancemetrics.utils.Constants;
import com.bs.dbperformancemetrics.utils.PerformanceMeasurementUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.bs.dbperformancemetrics.utils.PerformanceMeasurementUtils.measureExecutionTime;

@Service
public class MongoDBMongoPerformanceService implements IDatabasePerformanceService {

    private final MongoDBUserUploader userUploader;

    private final MongoDBUserMongoServiceImp userService;

    private final MongoDBConfig config;

    private final PerformanceResultModifier performanceResultModifier;

    public MongoDBMongoPerformanceService(MongoDBUserUploader userUploader, MongoDBUserMongoServiceImp userService, PerformanceResultModifier performanceResultModifier, MongoDBConfig config) {
        this.userUploader = userUploader;
        this.userService = userService;
        this.performanceResultModifier = performanceResultModifier;
        this.config = config;
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
        config.deactivateIndexes();
    }

    @Override
    public void seedDatabase() {
        final int startUserId = 1;
        List<MongoDBUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA, startUserId);
        userService.insertAll(users);
        config.reactivateIndexes();
    }

    @Override
    public void initializeDatabase() {
        prepareEmptyDatabase();
        resetDatabase();
        seedDatabase();
    }

    @Override
    public PerformanceResult saveUsers(int numberOfData) {
        final int startUserId = 1;
        List<MongoDBUser> users = userUploader.generateRandomUsers(numberOfData, startUserId);

        final long executionTime = measureExecutionTime(this::prepareEmptyDatabase, () -> {
            userService.insertAll(users);
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
            long time1 = System.currentTimeMillis();
            prepareEmptyDatabase();
            long time2 = System.currentTimeMillis();
            System.out.println("delete reset: " + (time2 - time1) / 1000.0 + "s");
            time1 = System.currentTimeMillis();
            userService.insertAll(users);
            time2 = System.currentTimeMillis();
            System.out.println("insert all: " + (time2 - time1) / 1000.0 + "s");
        }, () -> {
            userService.insert(addUser);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public List<PerformanceResult> compareUpsertInsert() {
        final int startUserId = 1;
        List<MongoDBUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA, startUserId);

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

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTimeUpsert), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Insert", executionTimeInsert)));
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

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First idx", executionTimeBeginning), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle idx", executionTimeMiddle), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last idx", executionTimeEnd)));
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

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First", executionTimeBeginning), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle", executionTimeMiddle), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last", executionTimeEnd)));
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

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First", executionTimeBeginning), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle", executionTimeMiddle), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last", executionTimeEnd)));
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
        }).toList();

        final long averageExecutionTimeByName = executionTimes.stream().mapToLong(Pair::getFirst).sum() / usernames.size();
        final long averageExecutionTimeByUsername = executionTimes.stream().mapToLong(Pair::getSecond).sum() / usernames.size();

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find By Non-Idx", averageExecutionTimeByName), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find By Idx", averageExecutionTimeByUsername)));
    }

    @Override
    public PerformanceResult updateAllUsers() {

        initializeDatabase();

        List<MongoDBUser> users = userService.findAll();

        List<MongoDBUser> updatedUsers = new ArrayList<>(users);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        final long executionTime = measureExecutionTime(() -> {
            userService.saveAll(users);
        }, () -> {
            userService.updateAll(updatedUsers);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public PerformanceResult updateUserById() {

        initializeDatabase();

        List<MongoDBUser> users = userService.findAll();

        List<MongoDBUser> selectedUsers = List.of(users.get(0), users.get(Constants.NUMBER_OF_DATA / 2), users.get(Constants.NUMBER_OF_DATA - 1));

        List<MongoDBUser> updatedUsers = new ArrayList<>(selectedUsers);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        final long executionTime = measureExecutionTime(() -> {
            userService.saveAll(selectedUsers);
        }, () -> {
            updatedUsers.forEach(userService::update);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public PerformanceResult updateUserByIndexedField() {

        initializeDatabase();

        List<MongoDBUser> users = userService.findAll();

        List<MongoDBUser> selectedUsers = List.of(users.get(0), users.get(Constants.NUMBER_OF_DATA / 2), users.get(Constants.NUMBER_OF_DATA - 1));

        final long executionTime = measureExecutionTime(() -> {
            userService.saveAll(selectedUsers);
        }, () -> {
            selectedUsers.forEach(user -> userService.updatePasswordByUsername(user.getUsername(), "updatedPassword"));
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public PerformanceResult updateUserByNonIndexedField() {

        initializeDatabase();

        List<MongoDBUser> users = userService.findAll();

        List<MongoDBUser> selectedUsers = List.of(users.get(0), users.get(Constants.NUMBER_OF_DATA / 2), users.get(Constants.NUMBER_OF_DATA - 1));

        final long executionTime = measureExecutionTime(() -> {
            userService.saveAll(selectedUsers);
        }, () -> {
            selectedUsers.forEach(user -> userService.updatePasswordByName(user.getName(), "updatedPassword"));
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public List<PerformanceResult> compareUpsertUpdate() {

        initializeDatabase();

        List<MongoDBUser> users = userService.findAll();

        List<MongoDBUser> updatedUsers = new ArrayList<>(users);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        final long executionTimeUpdate = measureExecutionTime(() -> {
            userService.saveAll(users);
        }, () -> {
            userService.updateAll(updatedUsers);
        });

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTimeUpdate)));
    }

    @Override
    public PerformanceResult deleteAllUsers() {
        final long executionTime = PerformanceMeasurementUtils.measureExecutionTime(() -> {
            initializeDatabase();
            userService.deleteAll();
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete", executionTime));
    }

    @Override
    public PerformanceResult deleteUserById() {

        Supplier<List<String>> selectedUsersSupplier = () -> {
            initializeDatabase();

            List<MongoDBUser> users = userService.findAll();

            return List.of(users.get(0).getId(), users.get(Constants.NUMBER_OF_DATA / 2).getId(), users.get(Constants.NUMBER_OF_DATA - 1).getId());
        };

        Consumer<List<String>> stringListConsumer = list -> {
            list.forEach(userService::deleteById);
        };

        final long executionTime = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete", executionTime));
    }

    @Override
    public PerformanceResult deleteUserByUsername() {

        final int startUserId = 1;
        List<MongoDBUser> newUsers = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA / 2, startUserId);

        Supplier<List<String>> selectedUsersSupplier = () -> {
            prepareEmptyDatabase();
            resetDatabase();
            userService.saveAll(newUsers);

            List<MongoDBUser> users = userService.findAll();

            return List.of(users.get(0).getUsername(), users.get(users.size() / 2).getUsername(), users.get(users.size() - 1).getUsername());
        };

        Consumer<List<String>> stringListConsumer = list -> {
            list.forEach(userService::deleteByUsername);
        };

        final long executionTime = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete By Idx", executionTime));
    }

    @Override
    public PerformanceResult deleteUserByName() {

        final int startUserId = 1;
        List<MongoDBUser> newUsers = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA / 2, startUserId);

        Supplier<List<String>> selectedUsersSupplier = () -> {
            prepareEmptyDatabase();
            resetDatabase();
            userService.saveAll(newUsers);

            List<MongoDBUser> users = userService.findAll();

            return List.of(users.get(0).getName(), users.get(users.size() / 2).getName(), users.get(users.size() - 1).getName());
        };

        Consumer<List<String>> stringListConsumer = list -> {
            list.forEach(userService::deleteByName);
        };

        final long executionTime = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete By N-Idx", executionTime));
    }

}
