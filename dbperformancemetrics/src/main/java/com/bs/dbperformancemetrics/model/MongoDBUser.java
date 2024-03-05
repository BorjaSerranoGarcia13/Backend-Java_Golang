package com.bs.dbperformancemetrics.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "mongoDBUser")
public class MongoDBUser implements IUser<String> {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String username;

    private String password;

    private List<String> friendIds;

    public MongoDBUser() {
    }

    public MongoDBUser(MongoDBUser user) {
        this.id = user.getId();
        this.name = user.getName();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.friendIds = user.getFriends();
    }

    public MongoDBUser(String name, String username, String password) {
        setName(name);
        setUsername(username);
        setPassword(password);
        this.friendIds = new ArrayList<>();
    }

    @Override
    public String getId() {
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
    public List<String> getFriends() {
        return friendIds;
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
    public void setFriends(List<String> friendIds) {
        if (friendIds == null) {
            throw new IllegalArgumentException("Friends list cannot be null");
        }
        for (String friendId : friendIds) {
            if (friendId == null || friendId.isEmpty()) {
                throw new IllegalArgumentException("Friend ID cannot be null or empty");
            }
        }
        this.friendIds = friendIds;
    }

    @Override
    public void addFriend(String friendId) {
        if (friendId == null || friendId.isEmpty()) {
            throw new IllegalArgumentException("Friend ID cannot be null or empty");
        }
        friendIds.add(friendId);
    }

    @Override
    public void removeFriend(String friendId) {
        if (friendId == null || friendId.isEmpty()) {
            throw new IllegalArgumentException("Friend ID cannot be null or empty");
        }
        if (!friendIds.contains(friendId)) {
            throw new IllegalArgumentException("Friend ID not found in the list");
        }
        friendIds.remove(friendId);
    }

    @Override
    public String toString() {
        return "User{" +
               "id='" + id + '\'' +
               ", name='" + name + '\'' +
               ", username='" + username + '\'' +
               ", password='" + password + '\'' +
               ", friends=" + friendIds +
               '}';
    }
}
