package com.bs.dbperformancemetrics.model;

import java.util.List;

public interface IUser<ID> {
    ID getId();

    String getName();

    String getUsername();

    String getPassword();

    List<ID> getFriendIds();

    void setName(String name);

    void setUsername(String username);

    void setPassword(String password);

    void setFriendIds(List<ID> friends);

    void addFriendId(ID friend);

    void removeFriendId(ID friend);

}
