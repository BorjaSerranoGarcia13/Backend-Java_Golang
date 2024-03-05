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

    void updateAll(List<T> t);

    void update(T t);

    void updatePasswordByUsername(String username, String newPassword);

    void updatePasswordByName(String name, String newPassword);

    void deleteAll();

    void deleteById(ID id);

    void deleteByUsername(String username);

    void deleteByName(String name);

    void addFriend(ID userId, ID friendId);

    void removeFriend(ID userId, ID friendId);

    T createCopyOfUser(T originalUser);

    List<T> createCopyOfUserList(List<T> originalList);
}
