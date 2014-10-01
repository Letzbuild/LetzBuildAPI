package com.letzbuild.API;

import com.mongodb.*;

import java.util.List;

public class App {

    public static void main(String[] args) throws Exception {
        MongoClient c = new MongoClient(new ServerAddress("localhost", 27017));

        DB db = c.getDB("test");
        final DBCollection dbCol = db.getCollection("users");

        DBCursor cursor1 = dbCol.find();
        List<DBObject> posts;
        try {
            posts = cursor1.toArray();
        } finally {
            cursor1.close();
        }


        System.out.println(posts);

        DBObject query = new BasicDBObject("education", "Diploma")
                .append("age", new BasicDBObject("$gt", 20))
                .append("city", "Chennai");
        DBCursor cur = dbCol.find(new BasicDBObject(), new BasicDBObject("_id", false));
        try {
            while(cur.hasNext()) {
                DBObject cursor = cur.next();
                System.out.println(cursor);
            }
        } finally {
            cur.close();
        }
    }
}

/* db.zips.aggregate([ {$group : {_id:"$state", average_pop:{$avg:"$pop"}}}]
db.zips.aggregate([ {$group : {_id:"$city", postal_codes:{$addToSet:"$_id"}}}])
db.zips.aggregate([ {$group : {_id:"$state", pop:{$max:"$pop"}}}])

db.fun.aggregate([{$group:{_id:{a:"$a", b:"$b"}, c:{$max:"$c"}}}, {$group:{_id:"$_id.a", c:{$min:"$c"}}}])

db.zips.aggregate([{$project:{_id:0, city:{$toLower:"$city"}, pop:1, state:1, zip:"$_id"}}])

db.zips.aggregate([{$match: {state:"CA"}}, {$group : {_id:"$city", population:{$sum:"$pop"}, zip_codes:{$addToSet:"$_id"}}}])

db.zips.aggregate([{$match: {state:"CA"}}, {$group : {_id:"$city", population:{$sum:"$pop"}, zip_codes:{$addToSet:"$_id"}}},
    {$project:{_id:0, city:"$_id", population:1, zip_codes:1}}
])

db.zips.aggregate([{$match: {state:"CA"}}, {$group : {_id:"$city", population:{$sum:"$pop"}, zip_codes:{$addToSet:"$_id"}}},
    {$project:{_id:0, city:"$_id", population:1, zip_codes:1}}, {$sort: {population:-1}}
])

db.zips.aggregate([{$match: {state:"CA"}}, {$group : {_id:"$city", population:{$sum:"$pop"}, zip_codes:{$addToSet:"$_id"}}},
    {$project:{_id:0, city:"$_id", population:1, zip_codes:1}}, {$sort: {population:-1}}, {$skip:10}, {$limit:5}
])

db.zips.aggregate([{$group : {_id:"$state", city:"$city"}, population:{$sum:"$pop"}},
    {$sort: {"_id.state":1, population:-1}}, {$group:{_id:"$_id.city", city:{$first: "$_id.city"}, population:{$first:"$population"}}}
])


db.zips.aggregate([{$match: {pop:{$gt:100000}}}])
db.zips.aggregate([{$sort:{state:1, city:1}}])
)*/

