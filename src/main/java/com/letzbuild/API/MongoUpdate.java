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
        DBCollection prodCol = db.getCollection("products");
        DBCollection prodSuppCol = db.getCollection("product_supplier_map");

        for(int i = 1; i < 67; ++i) {
            DBObject res = prodCol.findOne(new BasicDBObject("code", "LB"+i));
            System.out.println(res.get("code") + " - " + res.get("category"));
            prodSuppCol.update(new BasicDBObject("pcode", res.get("code")),
                    new BasicDBObject("$set", new BasicDBObject("purl", res.get("url"))), true, true);
            //prodCol.update(new BasicDBObject("code", "LB" + i),
            //          new BasicDBObject("$set", new BasicDBObject("url", "LB" + i + ".jpg")), true, true);
        }
    }
}
