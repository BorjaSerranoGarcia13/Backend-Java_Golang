package com.bs.dbperformancemetrics.model;

import java.util.List;

public interface IUser<ID> {
    ID getId();

    String getName();

    String getUsername();

    String getPassword();

    List<ID> getFriends();

    void setName(String name);

    void setUsername(String username);

    void setPassword(String password);

    void setFriends(List<ID> friends);

    void addFriend(ID friend);

    void removeFriend(ID friend);

}
