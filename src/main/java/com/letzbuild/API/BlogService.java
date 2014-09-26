package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;

import java.util.List;

public class BlogService {

    private DBCollection postsCollection;

    public BlogService(final DB letzbuildDB) {
        postsCollection = letzbuildDB.getCollection("posts");
    }

    public List<DBObject> getAllPosts(int limit) {
        List<DBObject> posts;
        DBCursor cursor = postsCollection.find().sort(new BasicDBObject().append("date", -1)).limit(limit);
        try {
            posts = cursor.toArray();
        } finally {
            cursor.close();
        }
        return posts;
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
