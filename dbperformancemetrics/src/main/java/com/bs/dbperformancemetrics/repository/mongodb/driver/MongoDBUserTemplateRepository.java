package com.bs.dbperformancemetrics.repository.mongodb.driver;

import com.bs.dbperformancemetrics.model.MongoDBUser;
import com.bs.dbperformancemetrics.repository.BaseRepository;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MongoDBUserTemplateRepository implements BaseRepository<MongoDBUser, String> {

    private final MongoTemplate mongoTemplate;

    public MongoDBUserTemplateRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void insert(MongoDBUser user) {
        mongoTemplate.insert(user);
    }

    @Override
    public void insertAll(List<MongoDBUser> users) {
        mongoTemplate.insertAll(users);
    }

    @Override
    public void save(MongoDBUser user) {
        mongoTemplate.save(user);
    }

    @Override
    public void saveAll(List<MongoDBUser> users) {
        List<WriteModel<Document>> bulkUpsertOperations = new ArrayList<>();
        UpdateOptions updateOptions = new UpdateOptions().upsert(true);

        for (MongoDBUser user : users) {
            Document queryDocument = new Document();
            queryDocument.put("_username", user.getUsername());

            bulkUpsertOperations.add(new UpdateOneModel<>(
                    queryDocument,
                    Updates.combine(
                            Updates.set("name", user.getName()),
                            Updates.set("username", user.getUsername()),
                            Updates.set("password", user.getPassword()),
                            Updates.set("friends", user.getFriends())
                    ),
                    updateOptions
            ));
        }

        mongoTemplate.getCollection(mongoTemplate.getCollectionName(MongoDBUser.class)).bulkWrite(bulkUpsertOperations);
    }

    @Override
    public List<MongoDBUser> findAll() {
        return mongoTemplate.findAll(MongoDBUser.class);
    }

    @Override
    public Optional<MongoDBUser> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, MongoDBUser.class));
    }

    @Override
    public Optional<MongoDBUser> findByUsername(String username) {
        return Optional.ofNullable(mongoTemplate.findOne(Query.query(Criteria.where("username").is(username)), MongoDBUser.class));
    }

    @Override
    public List<MongoDBUser> findByName(String name) {
        return mongoTemplate.find(Query.query(Criteria.where("name").is(name)), MongoDBUser.class);
    }

    @Override
    public Optional<String> findPasswordByUsername(String username) {
        Query query = new Query();
        query.addCriteria(Criteria.where("username").is(username));
        query.fields().include("password");
        MongoDBUser user = mongoTemplate.findOne(query, MongoDBUser.class);
        return user != null ? Optional.ofNullable(user.getPassword()) : Optional.empty();
    }

    @Override
    public void update(MongoDBUser user) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(user.getId()));
        Update update = new Update();
        update.set("name", user.getName());
        update.set("username", user.getUsername());
        update.set("password", user.getPassword());
        update.set("friends", user.getFriends());
        mongoTemplate.updateFirst(query, update, MongoDBUser.class);
    }

    @Override
    public void updateAll(List<MongoDBUser> users) {
        List<WriteModel<Document>> bulkOperations = new ArrayList<>();

        for (MongoDBUser user : users) {
            Document queryDocument = new Document();
            queryDocument.put("_id", user.getId());

            bulkOperations.add(new UpdateOneModel<>(
                    queryDocument,
                    Updates.combine(
                            Updates.set("name", user.getName()),
                            Updates.set("username", user.getUsername()),
                            Updates.set("password", user.getPassword()),
                            Updates.set("friends", user.getFriends())
                    )
            ));
        }

        mongoTemplate.getCollection(mongoTemplate.getCollectionName(MongoDBUser.class)).bulkWrite(bulkOperations);
    }

    @Override
    public void deleteById(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").is(id));
        mongoTemplate.remove(query, MongoDBUser.class);
    }

    @Override
    public void deleteAll() {
        mongoTemplate.remove(new Query(), MongoDBUser.class);
    }
}