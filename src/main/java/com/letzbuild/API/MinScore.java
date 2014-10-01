package com.letzbuild.API;
import com.mongodb.*;

/**
 * Created by venky on 25/08/14.
 */
public class MinScore {
    public static void main(String[]args) throws Exception {
        MongoClient c = new MongoClient(new ServerAddress("localhost", 27017));

        DB db = c.getDB("school");
        final DBCollection dbCol = db.getCollection("students");
        System.out.println(dbCol.count());

        DBCursor cur = dbCol.find(new BasicDBObject("scores.type", "homework"));
        try {
            while(cur.hasNext()) {
                DBObject cursor = cur.next();

                BasicDBList scores = (BasicDBList) cursor.get("scores");

                // create a new BasicDBList that is corrected of duplicate homework
                BasicDBList newList = new BasicDBList();

                double minHwScore = (double)0.0;
                for (Object eachScore : scores) {
                    double score = (double) ((DBObject) eachScore).get("score");
                    String type = (String) ((DBObject) eachScore).get("type");

                    if (!type.equals("homework")) {
                        newList.add(new BasicDBObject("type", type).append("score", score));
                    } else {
                        if (Double.compare(minHwScore, (double)0.0) == 0 ) {
                            minHwScore = score;
                        }
                        if (Double.compare(minHwScore, score) > 0) {
                            minHwScore = score;
                        }
                    }
                }
                newList.add(new BasicDBObject("type", "homework").append("score", minHwScore));

                cursor.put("scores", newList);
                dbCol.save(cursor);

                System.out.println(cursor);
            }
        } finally {
            cur.close();
        }
    }
}
