package com.letzbuild.API;

import com.mongodb.*;
import org.bson.types.ObjectId;

/**
 * Created by venky on 22/08/14.
 */
public class MongoUpdate {
    public static void main(String[] args) throws Exception {
        MongoClient c = new MongoClient(new MongoClientURI("mongodb://subha:demo1234@kahana.mongohq.com:10049/letzbuild"));

        DB db = c.getDB("letzbuild");
        DBCollection dbCol = db.getCollection("products");

        //dbCol.update(new BasicDBObject("_id", new ObjectId("53f5c4b50364420830570b05")),
        //    new BasicDBObject("$set", new BasicDBObject("age", 36)));

        BasicDBList newList = new BasicDBList();
        newList.add(new BasicDBObject("type", "homework1").append("score", 24).append("class", "a+"));

        dbCol.update(new BasicDBObject("_id", new ObjectId("53fc4e4051941ad3900b5275")),
            new BasicDBObject("$addToSet", new BasicDBObject("education",
                    new BasicDBObject("type", "homework").append("score", 23).append("class", "a")
                    //newList
            )), true, true);


        System.out.println(dbCol);
    }
}
