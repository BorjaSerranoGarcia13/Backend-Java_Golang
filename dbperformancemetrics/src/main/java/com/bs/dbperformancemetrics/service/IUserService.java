package com.bs.dbperformancemetrics.service;

import com.bs.dbperformancemetrics.model.IUser;

import java.util.List;

public interface IUserService<T extends IUser<ID>, ID> {

    void insert(T t);

    void insertAll(List<T> t);

    void save(T t);

    void saveAll(List<T> t);

    List<T> findAll();

    T findById(ID id);

    T findByUsername(String username);

    List<T> findByName(String name);

    String findPasswordByUsername(String username);

    void update(T t);

    void updateAll(List<T> t);

    void deleteById(ID id);

    void deleteAll();

    void changeUserName(ID userId, String newName);

    void changePassword(ID userId, String newPassword);

    void addFriend(ID userId, ID friendId);

    void removeFriend(ID userId, ID friendId);
}
