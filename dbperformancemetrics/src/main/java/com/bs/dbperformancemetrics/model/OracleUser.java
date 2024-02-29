package com.bs.dbperformancemetrics.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORACLE_USER",
        indexes = {@Index(name = "index_username", columnList = "username", unique = true)})
public class OracleUser implements IUser<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String username;
    private String password;

    @ElementCollection
    private List<Long> friendIds;

    public OracleUser() {
    }

    public OracleUser(String name, String username, String password) {
        setName(name);
        setUsername(username);
        setPassword(password);
        this.friendIds = new ArrayList<>();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public List<Long> getFriends() {
        return friendIds;
    }

    public void setId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID cannot be null or less than or equal to zero");
        }
        this.id = id;
    }

    @Override
    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.name = name;
    }

    @Override
    public void setUsername(String username) {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        this.password = password;
    }

    @Override
    public void setFriends(List<Long> friendIds) {
        if (friendIds == null) {
            throw new IllegalArgumentException("Friends list cannot be null");
        }
        for (Long friendId : friendIds) {
            if (friendId == null || friendId <= 0) {
                throw new IllegalArgumentException("Friend ID cannot be null or less than or equal to zero");
            }
        }
        this.friendIds = friendIds;
    }

    @Override
    public void addFriend(Long friendId) {
        if (friendId == null || friendId <= 0) {
            throw new IllegalArgumentException("Friend ID cannot be null or less than or equal to zero");
        }
        friendIds.add(friendId);
    }

    @Override
    public void removeFriend(Long friendId) {
        if (friendId == null || friendId <= 0) {
            throw new IllegalArgumentException("Friend ID cannot be null or less than or equal to zero");
        }
        if (!friendIds.contains(friendId)) {
            throw new IllegalArgumentException("Friend ID not found in the list");
        }
        friendIds.remove(friendId);
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", friends=" + friendIds +
               '}';
    }
}
