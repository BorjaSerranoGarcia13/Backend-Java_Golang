package com.bs.dbperformancemetrics.service.mongoDB;

import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.service.DatabaseUserJsonUploader;
import com.bs.dbperformancemetrics.service.mongoDB.mongo.MongoDBUserMongoServiceImp;
import com.bs.dbperformancemetrics.utils.RandomUserGenerator;
import com.bs.dbperformancemetrics.utils.UserJsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Service
public class MongoDBUserUploader extends DatabaseUserJsonUploader<String, MongoDBUser> {
    private final MongoDBUserMongoServiceImp MongoDBUserService;

    public MongoDBUserUploader(MongoDBUserMongoServiceImp MongoDBUserService,
                               RandomUserGenerator<String, MongoDBUser> randomUserGenerator,
                               UserJsonCreator<String, MongoDBUser> userJsonCreator) {
        super(randomUserGenerator, userJsonCreator);
        this.MongoDBUserService = MongoDBUserService;
    }

    @Override
    protected String getUserType() {
        return "MongoDBUser";
    }

    @Override
    public void uploadUsersFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<MongoDBUser> users = mapper.readValue(Paths.get(getJsonFilePath()).toFile(),
                new TypeReference<List<MongoDBUser>>() {
                });

        MongoDBUserService.saveAll(users);

    }
}
