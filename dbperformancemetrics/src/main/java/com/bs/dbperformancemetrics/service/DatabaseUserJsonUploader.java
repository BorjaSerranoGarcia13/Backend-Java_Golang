package com.bs.dbperformancemetrics.service;

import com.bs.dbperformancemetrics.model.IUser;
import com.bs.dbperformancemetrics.utils.RandomUserGenerator;
import com.bs.dbperformancemetrics.utils.UserJsonCreator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public abstract class DatabaseUserJsonUploader<ID, T extends IUser<ID>> {
    protected final RandomUserGenerator<ID, T> randomUserGenerator;
    protected final UserJsonCreator<ID, T> userJsonCreator;

    public DatabaseUserJsonUploader(RandomUserGenerator<ID, T> randomUserGenerator, UserJsonCreator<ID, T> userJsonCreator) {
        this.randomUserGenerator = randomUserGenerator;
        this.userJsonCreator = userJsonCreator;
    }

    public List<T> generateRandomUsers(int numberOfUsers, int startId) {
        return randomUserGenerator.generateRandomUsers(numberOfUsers, getUserType(), startId);
    }

    public void generateUserJson(int numberOfUsers, int startId) {
        try {
            List<T> users = generateRandomUsers(numberOfUsers, startId);

            userJsonCreator.createJsonFromUsers(users, getJsonFilePath());
        } catch (IOException e) {
            Logger logger = Logger.getLogger(DatabaseUserJsonUploader.class.getName());
            logger.log(Level.SEVERE, "Error generating and uploading JSON: ", e);
        }
    }

    protected String getJsonFilePath() {
        return "src/main/resources/json/" + getUserType() + ".json";
    }

    protected abstract String getUserType();

    protected abstract void uploadUsersFromJson() throws IOException;
}
