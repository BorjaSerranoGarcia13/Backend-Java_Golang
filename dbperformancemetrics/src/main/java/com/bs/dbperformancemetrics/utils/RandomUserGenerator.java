package com.bs.dbperformancemetrics.utils;

import com.bs.dbperformancemetrics.model.IUser;
import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.model.OracleUser;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RandomUserGenerator<ID, T extends IUser<ID>> {

    public List<T> generateRandomUsers(int numberOfUsers, String userType, int startId) {
        if (numberOfUsers <= 0) {
            throw new IllegalArgumentException("Number of users must be greater than 0");
        }
        if (startId <= 0) {
            throw new IllegalArgumentException("Start ID must be greater than 0");
        }

        List<T> users = new ArrayList<>();

        int finalId = startId + numberOfUsers;

        for (int i = startId; i < finalId; i++) {
            T user = createRandomUser(i, userType);
            UserValidation.validateUserCreation(user);
            users.add(user);
        }
        return users;
    }

    @SuppressWarnings("unchecked")
    private T createRandomUser(int nUser, String userType) {
        if (nUser <= 0) {
            throw new IllegalArgumentException("ID must be greater than 0");
        }

        if ("MongoDBUser".equals(userType)) {
            MongoDBUser user = new MongoDBUser("user" + nUser, "username" + nUser, "password" + nUser);
            return (T) user;
        } else if ("OracleUser".equals(userType)) {
            OracleUser user = new OracleUser("user" + nUser, "username" + nUser, "password" + nUser);
            return (T) user;
        } else {
            throw new IllegalArgumentException("Invalid user type: " + userType);
        }
    }
}