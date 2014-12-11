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
        //MongoClient c = new MongoClient(new MongoClientURI("mongodb://subha:demo1234@kahana.mongohq.com:10049/letzbuild"));
        MongoClient c = new MongoClient(new ServerAddress("localhost", 27017));

        DB db = c.getDB("letzbuild");
        DBCollection prodCol = db.getCollection("products");






        for(int i = 1; i < 67; ++i) {
            //DBObject res = prodCol.findOne(new BasicDBObject("code", "LB"+i));
            System.out.println("LB" + i);
            //prodCol.update(new BasicDBObject("code", "LB" + i),
            //        new BasicDBObject("$set", new BasicDBObject("code", "OLB" + i)), true, true);
            //prodEnqCol.update(new BasicDBObject("pcode", "LB" + i),
            //          new BasicDBObject("$set", new BasicDBObject("pcode", "OLB" + i)), true, true);
        }
    }

    private void createCategoryTree(DBCollection prodCol) {

    }
}
