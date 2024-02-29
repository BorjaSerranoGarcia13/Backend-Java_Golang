package com.bs.dbperformancemetrics.repository.mongodb.mongo;

import com.bs.dbperformancemetrics.model.MongoDBUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MongoDBUserMongoRepository extends MongoRepository<MongoDBUser, String> {

    Optional<MongoDBUser> findByUsername(String username);

    List<MongoDBUser> findByName(String name);

    @Query(value = "{ 'username' : ?0 }", fields = "{ 'password' : 1 }")
    Optional<String> findPasswordByUsername(String username);


}
