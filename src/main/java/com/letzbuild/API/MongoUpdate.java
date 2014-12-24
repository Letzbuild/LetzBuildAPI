package com.letzbuild.API;

import com.mongodb.*;
import org.bson.types.ObjectId;
import sun.misc.FloatingDecimal;

import java.util.*;

/**
 * Created by venky on 22/08/14.
 */
public class MongoUpdate {
    public static void main(String[] args) throws Exception {
        MongoClient c = new MongoClient(new MongoClientURI("mongodb://subha:demo1234@kahana.mongohq.com:10049/letzbuild"));
        //MongoClient c = new MongoClient(new ServerAddress("localhost", 27017));

        DB db = c.getDB("letzbuild");
        DBCollection prodCol = db.getCollection("products");
        DBCollection categoriesCol = db.getCollection("categories");
        DBCollection prodSuppMapCol = db.getCollection("product_supplier_map");
        DBCollection supplierCol = db.getCollection("suppliers");

        String [] catArr = {"Metal", "Minerals"};

        MongoUpdate mu = new MongoUpdate();

        //System.out.println("prepareProdSuppMap");
        //mu.prepareProdSuppMap(catArr, prodCol, supplierCol, prodSuppMapCol);

        /*System.out.println("createCategoryTree");
        mu.createCategoryTree(catArr, prodCol, categoriesCol);

        System.out.println("updateSupplierCounts");
        mu.updateSupplierCounts(prodSuppMapCol, categoriesCol);

        System.out.println("renameProductCategory");
        mu.renameProductCategory(prodCol);*/

        //System.out.println("addStarRating");
        //mu.addStarRating(supplierCol, prodSuppMapCol, prodCol);

        //System.out.println("getting rating info in prod supp mapping");
        //mu.addRatinginPSM(supplierCol, prodSuppMapCol);

       System.out.println("cleanup format of leoLoc");
       mu.geoLocCleanup(supplierCol);
    }

    private void addRatinginPSM(DBCollection supplierCol, DBCollection prodSuppMapCol) {
        DBCursor suppCursor = supplierCol.find();
        for (DBObject suppObj : suppCursor) {

            System.out.println(suppObj.get("code"));

            BasicDBObject target = new BasicDBObject("supplier",
                    new BasicDBObject("scode", suppObj.get("code"))
                            .append("name", suppObj.get("name"))
                            .append("rating", suppObj.get("rating")));

            prodSuppMapCol.update(new BasicDBObject("supplier.scode", suppObj.get("code")),
                        new BasicDBObject("$set", target), false, true);
        }
    }

    private void addStarRating(DBCollection supplierCol, DBCollection prodSuppMapCol, DBCollection prodCol) {
        DBCursor suppCursor = supplierCol.find(new BasicDBObject("rating", 5));
        for (DBObject suppObj : suppCursor) {

            System.out.println(suppObj.get("code"));


            DBCursor prodSuppCursor = prodSuppMapCol.find(new BasicDBObject("supplier.scode", suppObj.get("code")));
            for (DBObject prodSuppObj : prodSuppCursor) {
                System.out.println(prodSuppObj.get("pcode"));
                prodCol.update(new BasicDBObject("code", prodSuppObj.get("pcode")),
                        new BasicDBObject("$push", new BasicDBObject("starSuppliers",
                                new BasicDBObject("scode", suppObj.get("code")).append("name", suppObj.get("name")))));
            }
        }
    }

    private void renameProductCategory(DBCollection prodCol) {
        prodCol.update(new BasicDBObject(), new BasicDBObject("$unset", new BasicDBObject("category", "")), false, true);
        prodCol.update(new BasicDBObject(), new BasicDBObject("$rename", new BasicDBObject("subCategory", "category")), false, true);
    }

    private void updateSupplierCounts(DBCollection prodSuppMapCol, DBCollection categoriesCol) {
        //db.products.aggregate([{$group: {_id:"$category", cnt:{$sum:1}}}])

        Map<String, Object> dbObjIdMap = new HashMap<String, Object>();
        dbObjIdMap.put("category", "$category");
        dbObjIdMap.put("scode", "$supplier.scode");

        DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject(dbObjIdMap));
        groupFields.put("cnt", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject sort = new BasicDBObject("$sort", new BasicDBObject("category", 1));

        List<DBObject> pipeline = Arrays.asList(group, sort);

        AggregationOutput aggOutput = prodSuppMapCol.aggregate(pipeline);
        Iterable<DBObject> output = aggOutput.results();

        System.out.println(output);

        // now cycle through it and get the correct count.
        for (DBObject obj : output) {
            BasicDBObject dbo = (BasicDBObject)obj.get("_id");
            String scode = dbo.get("scode").toString();
            String category = dbo.get("category").toString();

            System.out.println(scode + "-" + category + "-" + obj.get("cnt"));

            categoriesCol.update(new BasicDBObject("subCats.cat", category),
                    new BasicDBObject("$inc", new BasicDBObject("subCats.$.cnt", obj.get("cnt")).append("subCats.$.suppCnt", 1)
                            ), false, true);
        }
    }

    private void prepareProdSuppMap(String [] catArr, DBCollection prodCol, DBCollection supplierCol,
                                    DBCollection prodSuppMapCol) {

        //DBObject inClause = new BasicDBObject("$in", catArr);

        DBCursor cursor = prodCol.find();
        for (DBObject obj : cursor) {
            System.out.println(obj.get("code"));
            prodSuppMapCol.update(new BasicDBObject("pcode", obj.get("code")),
                       new BasicDBObject("$set",
                               new BasicDBObject("category", obj.get("category"))
                                       .append("pname", obj.get("name")).append("purl", obj.get("url"))), false, true);
        }

        DBCursor sCursor = supplierCol.find();
        for (DBObject sobj : sCursor) {

            BasicDBObject target = new BasicDBObject("supplier",
                    new BasicDBObject("scode", sobj.get("code")).append("name", sobj.get("name")).append("rating", sobj.get("rating")));

            System.out.println(target);

            prodSuppMapCol.update(new BasicDBObject("scode", sobj.get("code")),
                    new BasicDBObject("$set", target), false, true);
        }
    }

    private void createCategoryTree(String [] catArr, DBCollection prodCol, DBCollection categoriesCol) {
        int i = 0;

        for (String category : catArr) {
            Iterable<DBObject> output = getCategoryTree(category, prodCol);


            BasicDBObject categoryObj = new BasicDBObject();
            // 20 is a starting number.
            categoryObj.put("_id", (1 + i));
            categoryObj.put("category", category);
            categoryObj.put("subCats", output);
            categoryObj.put("parent", null);
            categoryObj.put("cnt", categoryObj.size());

            // these counts are incorrect as there could be the same product repeated for different supplier.
            // so this is a dummy insert. We'll correct it when we update from
            // this has to be taken to the categories collection
            categoriesCol.insert(categoryObj);
            ++i;
        }
    }

    private Iterable<DBObject> getCategoryTree(String category, DBCollection prodCol) {
        Iterable<DBObject> output = null;

        // run an aggregation on products to get the product counts under a subcategory.
        System.out.println(category);

        //db.products.aggregate([{$group: {_id:"$subCategory", cnt:{$sum:1}}}])

        BasicDBObject match = new BasicDBObject("$match", new BasicDBObject("category", category));

        DBObject groupFields = new BasicDBObject("_id", "$subCategory");
        //groupFields.put("cnt", new BasicDBObject("$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields);

        List<DBObject> pipeline = Arrays.asList(match, group);

        AggregationOutput aggOutput = prodCol.aggregate(pipeline);
        output = aggOutput.results();

        for (DBObject obj : output) {
            String subCategory = obj.get("_id").toString();
            obj.put("cat", subCategory);
            obj.put("cnt", 0);
            obj.put("suppCnt", 0);
            //String prodCnt = obj.get("cnt").toString();
            obj.removeField("_id");
        }

        System.out.println(output.toString());

        return output;
    }

    private void geoLocCleanup(DBCollection supplierCol) {

        DBCursor cursor = supplierCol.find();
        for (DBObject obj : cursor) {
            BasicDBObject getLocObj = (BasicDBObject)obj.get("geoLoc");
            String loc = getLocObj.get("location").toString();

            List<String> latLon = Arrays.asList(loc.split(", "));

            System.out.println(latLon.get(0) + "-" + latLon.get(1));

            supplierCol.update(new BasicDBObject("code", obj.get("code")),
                    new BasicDBObject("$set", new BasicDBObject("geoLoc",
                            new BasicDBObject("lat", latLon.get(0)).append("lon", latLon.get(1))))
                    , false, true);
        }
    }
}
