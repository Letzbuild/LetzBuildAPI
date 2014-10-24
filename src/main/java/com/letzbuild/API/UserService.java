package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */
import com.mongodb.*;

import java.util.*;

public class UserService {

    private DBCollection usersCollection_;

    public UserService(final DB letzbuildDB) {
        usersCollection_ = letzbuildDB.getCollection("users");
    }

    public DBObject getUser(String email) {
        DBObject user = null;

        user = usersCollection_.findOne(new BasicDBObject("username", email));

        return user;
    }

    public void createUser(String username, String password) {
        InvalidInputs.failIfInvalid("email", username);
        InvalidInputs.failIfInvalid("password", password);

        DBObject doc = new BasicDBObject();
        doc.put("username", username);
        doc.put("password", password);
        usersCollection_.insert(doc);
    }

}
