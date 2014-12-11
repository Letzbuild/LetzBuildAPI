package com.letzbuild.API;

import com.mongodb.*;
import org.bson.types.ObjectId;

import java.util.*;

/**
 * Created by venky on 22/08/14.
 */
public class MongoUpdate {
    public static void main(String[] args) throws Exception {
        //MongoClient c = new MongoClient(new MongoClientURI("mongodb://subha:demo1234@kahana.mongohq.com:10049/letzbuild"));
        MongoClient c = new MongoClient(new ServerAddress("localhost", 27017));

        DB db = c.getDB("letzbuild");
        DBCollection prodCol = db.getCollection("products");

        MongoUpdate mu = new MongoUpdate();

        String [] catArr = {"Metal", "Minerals"};
        for (String category : catArr) {
            mu.createCategoryTree(category, prodCol);
        }
    }

    private void createCategoryTree(String category, DBCollection prodCol) {
        // run an aggregation on products to get the product counts under a subcategory.
        System.out.println(category);

        //db.products.aggregate([{$group: {_id:"$subCategory", cnt:{$sum:1}}}])

        BasicDBObject match = new BasicDBObject("$match", new BasicDBObject("category", category));

        DBObject groupFields = new BasicDBObject("_id", "$subCategory");
        groupFields.put("cnt", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);

        List<DBObject> pipeline = Arrays.asList(match, group);

        AggregationOutput aggOutput = prodCol.aggregate(pipeline);
        Iterable<DBObject> output = aggOutput.results();

        for (DBObject obj : output) {
            String subCategory = obj.get("_id").toString();
            String prodCnt = obj.get("cnt").toString();

            System.out.println(subCategory + "-" + prodCnt);
        }

    }

    private void foo() {
        for (int i = 1; i < 67; ++i) {
            //DBObject res = prodCol.findOne(new BasicDBObject("code", "LB"+i));
            System.out.println("LB" + i);
            //prodCol.update(new BasicDBObject("code", "LB" + i),
            //        new BasicDBObject("$set", new BasicDBObject("code", "OLB" + i)), true, true);
            //prodEnqCol.update(new BasicDBObject("pcode", "LB" + i),
            //          new BasicDBObject("$set", new BasicDBObject("pcode", "OLB" + i)), true, true);
        }
    }
}
