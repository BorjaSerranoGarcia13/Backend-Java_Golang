package com.bs.dbperformancemetrics.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import javax.annotation.PostConstruct;
import java.util.Set;

@Configuration
@EnableMongoRepositories(basePackages = "com.bs.dbperformancemetrics.repository.mongodb")
public class MongoDBConfig {
    private final MongoTemplate mongoTemplate;

    public MongoDBConfig(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void deleteAllCollectionsContents() {
        Set<String> collections = mongoTemplate.getCollectionNames();

        for (String collectionName : collections) {
            mongoTemplate.remove(new Query(), collectionName);
        }
    }
}
