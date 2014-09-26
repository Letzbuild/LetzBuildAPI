package com.pearson.API;

import com.mongodb.*;

import java.util.*;

/**
 * Created by venky on 21/08/14.
 */
public class MongoInsert {
    public static void main(String[] args) throws Exception {
        MongoClient c = new MongoClient(new ServerAddress("localhost", 27017));

        DB db = c.getDB("test");
        DBCollection dbCol = db.getCollection("users");

        DBObject doc = new BasicDBObject();
        doc.put("name", "Anupama");
        doc.put("Nationality", "US");
        doc.put("city", "Chennai");
        doc.put("education", new ArrayList<BasicDBObject>());

        System.out.println(doc);
       dbCol.insert(doc);

        //dbCol.insert(new BasicDBObject("name", "Anupama").append("Nationality", "American"));

        System.out.println(doc);
    }
}
