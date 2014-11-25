package com.letzbuild.API;

import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by venky on 22/08/14.
 */
public class MongoUpdate {
    public static void main(String[] args) throws Exception {
        MongoClient c = new MongoClient(new MongoClientURI("mongodb://subha:demo1234@kahana.mongohq.com:10049/letzbuild"));

        DB db = c.getDB("letzbuild");
        DBCollection dbCol = db.getCollection("products");

        List<String> elements = new ArrayList<>();
        elements.add("Mahaveer Chemicals");
        elements.add("Shah Silica");
        elements.add("Carlton Silica");

        dbCol.update(new BasicDBObject("category", "Sand"),
              new BasicDBObject("$set", new BasicDBObject("manufacturers", elements)), true, true);


        System.out.println(dbCol);
    }
}
