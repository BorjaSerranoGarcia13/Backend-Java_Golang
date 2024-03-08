package com.bs.dbperformancemetrics.service.databse.oracle.jpa;

import com.bs.dbperformancemetrics.config.JPAConfig;
import com.bs.dbperformancemetrics.model.OracleUser;
import com.bs.dbperformancemetrics.service.databse.oracle.OracleUserUploader;
import com.bs.dbperformancemetrics.service.databse.oracle.jdbc.OracleUserJDBCServiceImp;
import com.bs.dbperformancemetrics.service.performance.IDatabasePerformanceService;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResult;
import com.bs.dbperformancemetrics.service.performance.result.PerformanceResultModifier;
import com.bs.dbperformancemetrics.utils.Constants;
import com.bs.dbperformancemetrics.utils.PerformanceMeasurementUtils;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.bs.dbperformancemetrics.utils.PerformanceMeasurementUtils.measureExecutionTime;

@Service
public class OracleJPAPerformanceService implements IDatabasePerformanceService {
    private final OracleUserUploader userUploader;

    private final OracleUserJPAServiceImp userService;

    private final OracleUserJDBCServiceImp userJDBCService;

    private final JPAConfig config;

    private final PerformanceResultModifier performanceResultModifier;


    public OracleJPAPerformanceService(OracleUserUploader userUploader, OracleUserJPAServiceImp userService, JPAConfig config, PerformanceResultModifier performanceResultModifier, OracleUserJDBCServiceImp userJDBCService) {
        this.userUploader = userUploader;
        this.userService = userService;
        this.config = config;
        this.performanceResultModifier = performanceResultModifier;
        this.userJDBCService = userJDBCService;
    }

    @Override
    public String getDatabaseDetails() {
        String databaseName = "Oracle";
        String databaseImplementation = "JPA";

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
        userJDBCService.insertAll(users);
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
        }, () -> userService.insertAll(users));

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Insert", executionTime));
    }

    @Override
    public PerformanceResult saveUser() {

        final int endUserId = Constants.NUMBER_OF_DATA + 1;

        initializeDatabase();

        OracleUser addUser = new OracleUser("user" + endUserId, "username" + endUserId, "password" + endUserId);
        userJDBCService.insert(addUser);

        final long executionTime = measureExecutionTime(() -> userJDBCService.deleteByUsername("username" + endUserId),
                () -> userJDBCService.insert(addUser));

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Insert", executionTime));
    }

    @Override
    public List<PerformanceResult> compareUpsertInsert() {
        final int startUserId = 1;
        List<OracleUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA / 2, startUserId);

        final long executionTimeUpsert = measureExecutionTime(() -> {
            prepareEmptyDatabase();
            resetDatabase();
        }, () -> userService.saveAll(users));

        final long executionTimeInsert = measureExecutionTime(() -> {
            prepareEmptyDatabase();
            resetDatabase();
        }, () -> userService.insertAll(users));

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTimeUpsert), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Insert", executionTimeInsert)));
    }

    @Override
    public PerformanceResult findAllUsers() {
        initializeDatabase();

        final long executionTime = measureExecutionTime(userService::findAll);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find", executionTime));
    }

    @Override
    public List<PerformanceResult> findUserByUsername() {

        initializeDatabase();

        final long executionTimeBeginning = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findByUsername("username1"));

        final long executionTimeMiddle = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findByUsername("username" + Constants.NUMBER_OF_DATA / 2));

        final long executionTimeEnd = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findByUsername("username" + Constants.NUMBER_OF_DATA));

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First idx", executionTimeBeginning), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle idx", executionTimeMiddle), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last idx", executionTimeEnd)));
    }

    @Override
    public List<PerformanceResult> findUserByName() {
        initializeDatabase();

        final long executionTimeBeginning = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findByName("username1"));

        final long executionTimeMiddle = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findByName("username" + Constants.NUMBER_OF_DATA / 2));

        final long executionTimeEnd = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findByName("username" + Constants.NUMBER_OF_DATA));

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First", executionTimeBeginning), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle", executionTimeMiddle), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last", executionTimeEnd)));
    }

    @Override
    public List<PerformanceResult> findPasswordByUsername() {

        initializeDatabase();

        final long executionTimeBeginning = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findPasswordByUsername("username1"));

        final long executionTimeMiddle = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findPasswordByUsername("username" + Constants.NUMBER_OF_DATA / 2));

        final long executionTimeEnd = PerformanceMeasurementUtils.measureExecutionTime(() -> userService.findPasswordByUsername("username" + Constants.NUMBER_OF_DATA));

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find First", executionTimeBeginning), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Middle", executionTimeMiddle), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Find Last", executionTimeEnd)));
    }

    @Override
    public List<PerformanceResult> compareReadIndexAndNonIndex() {

        initializeDatabase();

        List<String> usernames = List.of("username1", "username" + Constants.NUMBER_OF_DATA / 2, "username" + Constants.NUMBER_OF_DATA);

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

        prepareEmptyDatabase();
        resetDatabase();

        List<OracleUser> users = userUploader.generateRandomUsers(Constants.NUMBER_OF_DATA / 2, 1);
        userJDBCService.saveAll(users);

        users = userService.findAll();

        List<OracleUser> updatedUsers = userService.createCopyOfUserList(users);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        List<OracleUser> finalUsers = users;
        final long executionTime = measureExecutionTime(() -> userJDBCService.saveAll(finalUsers), () -> userService.updateAll(updatedUsers));

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public PerformanceResult updateUserById() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(userService.findById(1L), userService.findById(Constants.NUMBER_OF_DATA / 2L), userService.findById((long) Constants.NUMBER_OF_DATA));

        List<OracleUser> updatedUsers = userService.createCopyOfUserList(selectedUsers);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        final long executionTime = measureExecutionTime(() -> userService.saveAll(selectedUsers), () -> userService.updateAll(updatedUsers));

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public PerformanceResult updateUserByIndexedField() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(userService.findById(1L), userService.findById(Constants.NUMBER_OF_DATA / 2L), userService.findById((long) Constants.NUMBER_OF_DATA));

        final long executionTime = measureExecutionTime(() -> userService.saveAll(selectedUsers), () -> selectedUsers.forEach(user -> userService.updatePasswordByUsername(user.getUsername(), "updatedPassword")));

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public PerformanceResult updateUserByNonIndexedField() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(userService.findById(1L), userService.findById(Constants.NUMBER_OF_DATA / 2L), userService.findById((long) Constants.NUMBER_OF_DATA));

        final long executionTime = measureExecutionTime(() -> userJDBCService.saveAll(selectedUsers), () -> selectedUsers.forEach(user -> userService.updatePasswordByName(user.getName(), "updatedPassword")));

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTime));
    }

    @Override
    public List<PerformanceResult> compareUpsertUpdate() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(userService.findById(1L), userService.findById(Constants.NUMBER_OF_DATA / 2L), userService.findById((long) Constants.NUMBER_OF_DATA));
        List<OracleUser> updatedUsers = userService.createCopyOfUserList(selectedUsers);
        updatedUsers.forEach(user -> user.setName("updatedName"));

        final long executionTimeUpsert = measureExecutionTime(() -> userJDBCService.saveAll(selectedUsers), () -> userService.saveAll(updatedUsers));

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Upsert", executionTimeUpsert)));
    }

    @Override
    public PerformanceResult deleteAllUsers() {
        final long executionTime = PerformanceMeasurementUtils.measureExecutionTime(this::initializeDatabase, userService::deleteAll);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete", executionTime));
    }

    @Override
    public PerformanceResult deleteUserById() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(userJDBCService.findById(1L), userJDBCService.findById(Constants.NUMBER_OF_DATA / 2L), userJDBCService.findById((long) Constants.NUMBER_OF_DATA));

        Supplier<List<Long>> selectedUsersSupplier = () -> getListSupplier(selectedUsers).get();

        Consumer<List<Long>> stringListConsumer = list -> list.forEach(userService::deleteById);

        final long executionTime = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete By ID", executionTime));
    }

    @Override
    public PerformanceResult deleteUserByUsername() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(userJDBCService.findById(1L), userJDBCService.findById(Constants.NUMBER_OF_DATA / 2L), userJDBCService.findById((long) Constants.NUMBER_OF_DATA));

        Supplier<List<String>> selectedUsersSupplier = () -> getListSupplier(selectedUsers, "getUsername").get();

        Consumer<List<String>> stringListConsumer = list -> list.forEach(userService::deleteByUsername);

        final long executionTime = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete By Idx", executionTime));
    }

    @Override
    public PerformanceResult deleteUserByName() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(userJDBCService.findById(1L), userJDBCService.findById(Constants.NUMBER_OF_DATA / 2L), userJDBCService.findById((long) Constants.NUMBER_OF_DATA));

        Supplier<List<String>> selectedUsersSupplier = () -> getListSupplier(selectedUsers, "getName").get();

        Consumer<List<String>> stringListConsumer = list -> list.forEach(userService::deleteByName);

        final long executionTime = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResult(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete By N-Idx", executionTime));
    }

    @Override
    public List<PerformanceResult> compareDeleteIndexAndNonIndex() {

        initializeDatabase();

        List<OracleUser> selectedUsers = List.of(userJDBCService.findById(1L), userJDBCService.findById(Constants.NUMBER_OF_DATA / 2L), userJDBCService.findById((long) Constants.NUMBER_OF_DATA));

        Supplier<List<String>> selectedUsersSupplier = () -> getListSupplier(selectedUsers, "getName").get();

        Consumer<List<String>> stringListConsumer = list -> list.forEach(userService::deleteByName);

        final long executionTimeByName = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        selectedUsersSupplier = () -> getListSupplier(selectedUsers, "getUsername").get();

        stringListConsumer = list -> list.forEach(userService::deleteByUsername);

        final long executionTimeByUsername = measureExecutionTime(selectedUsersSupplier, stringListConsumer);

        return performanceResultModifier.modifyPerformanceResults(List.of(performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete By N-Idx", executionTimeByName), performanceResultModifier.generatePerformanceResultString(getDatabaseDetails(), "Delete By Idx", executionTimeByUsername)));
    }

    private Supplier<List<String>> getListSupplier(List<OracleUser> selectedUsers, String methodName) {

        userJDBCService.saveAll(selectedUsers);

        return () -> {
            if ("getName".equals(methodName)) {
                return List.of(selectedUsers.get(0).getName(), selectedUsers.get(1).getName(), selectedUsers.get(2).getName());
            } else {
                return List.of(selectedUsers.get(0).getUsername(), selectedUsers.get(1).getUsername(), selectedUsers.get(2).getUsername());
            }
        };
    }

    private Supplier<List<Long>> getListSupplier(List<OracleUser> selectedUsers) {

        userJDBCService.saveAll(selectedUsers);

        return () -> List.of(selectedUsers.get(0).getId(), selectedUsers.get(1).getId(), selectedUsers.get(2).getId());

    }

}
