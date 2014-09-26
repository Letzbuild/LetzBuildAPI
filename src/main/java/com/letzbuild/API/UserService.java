package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */
import com.mongodb.*;

import java.util.*;

public class UserService {

    private DBCollection usersCollection;

    public UserService(final DB letzbuildDB) {
        usersCollection = letzbuildDB.getCollection("users");
    }

    public List<DBObject> getAllUsers() {
        List<DBObject> users;
        DBCursor cursor = usersCollection.find();
        try {
            users = cursor.toArray();
        } finally {
            cursor.close();
        }

        return users;
    }

    /*public User getUser(String id) {
        return null;//users.get(id);
    }*/

    /*public User createUser(String name, String email) {
        failIfInvalid(name, email);
        //User user = new User(name, email);
        //users.put(user.getId(), user);
        return user;
    }*/

    /*public User updateUser(String id, String name, String email) {
        //User user = null;//users.get(id);
        //if (user == null) {
        //    throw new IllegalArgumentException("No user with id '" + id + "' found");
        //}
        failIfInvalid(name, email);
        //user.setName(name);
        //user.setEmail(email);
        return null; //user;
    }*/

    private void failIfInvalid(String name, String email) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'email' cannot be empty");
        }
    }
}
