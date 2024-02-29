package com.bs.dbperformancemetrics.repository;

import java.util.List;
import java.util.Optional;

public interface BaseRepository<T, ID> {

    void insert(T t);

    void insertAll(List<T> t);

    void save(T t);

    void saveAll(List<T> t);

    List<T> findAll();

    Optional<T> findById(ID id);

    Optional<T> findByUsername(String username);

    List<T> findByName(String name);

    Optional<String> findPasswordByUsername(String username);

    void update(T t);

    void updateAll(List<T> t);

    void deleteById(ID id);

    void deleteAll();
}
