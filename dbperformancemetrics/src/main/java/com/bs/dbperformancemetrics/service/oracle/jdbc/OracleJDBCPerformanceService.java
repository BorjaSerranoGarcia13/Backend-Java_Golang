package com.bs.dbperformancemetrics.service.oracle.jdbc;

import com.bs.dbperformancemetrics.config.JDBCConfig;
import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.service.oracle.OracleUserUploader;
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
public class OracleJDBCPerformanceService implements IDatabasePerformanceService {

    private final OracleUserUploader userUploader;

    private final OracleUserJDBCServiceImp userService;

    private final JDBCConfig config;

    private final PerformanceResultModifier performanceResultModifier;

    public OracleJDBCPerformanceService(OracleUserUploader userUploader, OracleUserJDBCServiceImp userService,
                                        JDBCConfig config, PerformanceResultModifier performanceResultModifier) {
        this.userUploader = userUploader;
        this.userService = userService;
        this.config = config;
        this.performanceResultModifier = performanceResultModifier;
    }

    @Override
    public String getDatabaseDetails() {
        String databaseName = "Oracle";
        String databaseImplementation = "JDBC";

        return String.format(" %-15s | %-15s ", databaseName, databaseImplementation);
    }

    @Override
    public void prepareEmptyDatabase() {
        userService.deleteAll();
    }

    @Override
    public void resetDatabase() {
        config.resetAndCreateIDSequence();
    }

    @Override
    public void seedDatabase() {
        final int startUserId = 1;
        List<OracleUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA, startUserId);
        userService.insertAll(users);
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
        List<OracleUser> users = userUploader.generateRandomUsers(numberOfData, startUserId);

        final long executionTime = measureExecutionTime(() -> {
            prepareEmptyDatabase();
            resetDatabase();

        }, () -> {
            userService.insertAll(users);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Insert", executionTime));
    }

    @Override
    public PerformanceResult saveUser() {
        final int startUserId = 1;
        final int endUserId = Constants.NUMBER_OF_DATA + 1;

        List<OracleUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA, startUserId);
        OracleUser addUser = new OracleUser("user" + endUserId, "username" + endUserId, "password" + endUserId);

        final long executionTime = measureExecutionTime(() -> {
            prepareEmptyDatabase();
            resetDatabase();
            userService.insertAll(users);
        }, () -> {
            userService.insert(addUser);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Insert", executionTime));
    }

    @Override
    public List<PerformanceResult> compareUpsertInsert() {
        final int startUserId = 1;
        List<OracleUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA, startUserId);

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

        initializeDatabase();

        List<OracleUser> users = userService.findAll();

        List<OracleUser> updatedUsers = new ArrayList<>(users);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        final long executionTime = measureExecutionTime(() -> {
            userService.saveAll(users);
        }, () -> {
            userService.updateAll(updatedUsers);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Update", executionTime));
    }

    @Override
    public PerformanceResult updateUserById() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(
                userService.findById(1L),
                userService.findById(Constants.NUMBER_OF_DATA / 2L),
                userService.findById((long) Constants.NUMBER_OF_DATA)
        );

        List<OracleUser> updatedUsers = new ArrayList<>(selectedUsers);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        final long executionTime = measureExecutionTime(() -> {
            userService.saveAll(selectedUsers);
        }, () -> {
            updatedUsers.forEach(userService::update);
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Update", executionTime));
    }

    @Override
    public PerformanceResult updateUserByIndexedField() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(
                userService.findById(1L),
                userService.findById(Constants.NUMBER_OF_DATA / 2L),
                userService.findById((long) Constants.NUMBER_OF_DATA)
        );

        final long executionTime = measureExecutionTime(() -> {
            userService.saveAll(selectedUsers);
        }, () -> {
            selectedUsers.forEach(user ->
                    userService.updatePasswordByUsername(user.getUsername(), "updatedPassword")
            );
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Update", executionTime));
    }

    @Override
    public PerformanceResult updateUserByNonIndexedField() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(
                userService.findById(1L),
                userService.findById(Constants.NUMBER_OF_DATA / 2L),
                userService.findById((long) Constants.NUMBER_OF_DATA)
        );

        final long executionTime = measureExecutionTime(() -> {
            userService.saveAll(selectedUsers);
        }, () -> {
            selectedUsers.forEach(user ->
                    userService.updatePasswordByName(user.getName(), "updatedPassword")
            );
        });

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Update", executionTime));
    }

    @Override
    public List<PerformanceResult> compareUpsertUpdate() {

        initializeDatabase();

        List<OracleUser> users = userService.findAll();

        List<OracleUser> updatedUsers = new ArrayList<>(users);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        final long executionTimeUpsert = measureExecutionTime(() -> {
            userService.saveAll(users);
        }, () -> {
            userService.saveAll(updatedUsers);
        });

        final long executionTimeUpdate = measureExecutionTime(() -> {
            userService.saveAll(users);
        }, () -> {
            userService.updateAll(updatedUsers);
        });

        return performanceResultModifier.modifyPerformanceResults(List.of(
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTimeUpsert),
                performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Update", executionTimeUpdate)));
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

        final int startUserId = 1;
        List<OracleUser> newUsers = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA / 2, startUserId);

        Supplier<List<Long>> selectedUsersSupplier = () -> {
            prepareEmptyDatabase();
            resetDatabase();
            userService.saveAll(newUsers);

            List<OracleUser> users = userService.findAll();

            return List.of(
                    users.get(0).getId(),
                    users.get(users.size() / 2).getId(),
                    users.get(users.size() - 1).getId()
            );
        };

        Consumer<List<Long>> stringListConsumer = list -> {
            list.forEach(userService::deleteById);
        };

        final long executionTime = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete", executionTime));
    }

    @Override
    public PerformanceResult deleteUserByUsername() {

        final int startUserId = 1;
        List<OracleUser> newUsers = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA / 2, startUserId);

        Supplier<List<String>> selectedUsersSupplier = () -> {
            prepareEmptyDatabase();
            resetDatabase();
            userService.saveAll(newUsers);

            List<OracleUser> users = userService.findAll();

            return List.of(
                    users.get(0).getUsername(),
                    users.get(users.size() / 2).getUsername(),
                    users.get(users.size() - 1).getUsername()
            );
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
        List<OracleUser> newUsers = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA / 2, startUserId);

        Supplier<List<String>> selectedUsersSupplier = () -> {
            prepareEmptyDatabase();
            resetDatabase();
            userService.saveAll(newUsers);

            List<OracleUser> users = userService.findAll();

            return List.of(
                    users.get(0).getName(),
                    users.get(users.size() / 2).getName(),
                    users.get(users.size() - 1).getName()
            );
        };

        Consumer<List<String>> stringListConsumer = list -> {
            list.forEach(userService::deleteByName);
        };

        final long executionTime = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete By N-Idx", executionTime));
    }


}
