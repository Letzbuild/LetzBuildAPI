package com.letzbuild.API;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.io.IOException;

/**
 * Created by venky on 22/09/14.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String mongoURIString = (args.length == 0) ? "mongodb://localhost" : args[0];
        mongoURIString = "mongodb://subha:demo1234@kahana.mongohq.com:10049/letzbuild";

        final MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        final DB letzbuildDB = mongoClient.getDB("letzbuild");

        new UserController(new UserService(letzbuildDB));
        new BlogController(new BlogService(letzbuildDB));
    }
}
