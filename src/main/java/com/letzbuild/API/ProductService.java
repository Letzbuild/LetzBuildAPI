package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import spark.Request;

import java.util.*;
import java.util.regex.Pattern;

public class ProductService {

    private DBCollection productsCollection_;
    private DBCollection categoriesCollection_;
    private DBCollection prodSupMapCollection_;

    private Properties p_;

    public ProductService(final DB letzbuildDB, final Properties p) {
        productsCollection_ = letzbuildDB.getCollection("products");
        categoriesCollection_ = letzbuildDB.getCollection("categories");
        prodSupMapCollection_ = letzbuildDB.getCollection("product_supplier_map");
        p_ = p;
    }

    public List<DBObject> retrieveCategories() {
        List<DBObject> categories = null;

        //db.categories.find({parent: null})
        BasicDBObject query = new BasicDBObject();
        query.append("parent", null);

        DBCursor cursor = categoriesCollection_.find(query);
        try {
            categories = cursor.toArray();
        } finally {
            cursor.close();
        }

        return categories;
    }

    public List<DBObject> retrieveCategories(String category) {
        List<DBObject> categories = null;

        BasicDBObject query = new BasicDBObject();

        query.append("_id", category);


        DBCursor cursor = categoriesCollection_.find(query);
        try {
            categories = cursor.toArray();
        } finally {
            cursor.close();
        }

        return categories;
    }

    public List<DBObject> searchProducts(Request req) {
        List<DBObject> products = null;

        int limit = Integer.parseInt(p_.getProperty("pageLimit"));
        String limitStr = req.queryParams("limit");
        if ((limitStr != null) && (limitStr.length() > 0)) {
            limit = Integer.parseInt(req.queryParams("limit"));
        }

        BasicDBObject query = new BasicDBObject();

        String keyword = req.queryParams("word");
        if ((keyword != null) && (keyword.length() > 0)) {
            //db.products.find({searchDesc: {$regex:/blue/i}})

            String pattern = ".*\\b" + keyword + "\\b.*";
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            query.append("searchDesc", regex);
        }

        String category = req.queryParams("cat");
        if ((category != null) && (category.length() > 0)) {
            //db.products.find({category: "Steel")

            query.append("category", category);
        }

        DBCursor cursor = productsCollection_.find(query).limit(limit);
        try {
            products = cursor.toArray();
        } finally {
            cursor.close();
        }

        return products;
    }

    public List<DBObject> retrieveProducts(Request req) {
        List<DBObject> products = null;

        int limit = Integer.parseInt(p_.getProperty("pageLimit"));
        String limitStr = req.queryParams("limit");
        if ((limitStr != null) && (limitStr.length() > 0)) {
            limit = Integer.parseInt(limitStr);
        }

        int page = 1;
        String pageStr = req.queryParams("page");
        if ((pageStr != null) && (pageStr.length() > 0)) {
            page = Integer.parseInt(pageStr);
        }
        // the skips go from 0 onwards.
        --page;

        BasicDBObject query = new BasicDBObject();

        String category = req.queryParams("cat");
        if ((category != null) && (category.length() > 0)) {
            //db.products.find({category: "Steel"})

            query.append("category", category);
        }

        DBCursor cursor = productsCollection_.find(query, prepareProductFields()).skip(page * limit).limit(limit);
        try {
            products = cursor.toArray();
        } finally {
            cursor.close();
        }

        return products;
    }

    public Iterable<DBObject> retrieveProductsForSupplier(Request req) {
        Iterable<DBObject> output = null;

        int lmt = Integer.parseInt(p_.getProperty("pageLimit"));
        String limitStr = req.queryParams("limit");
        if ((limitStr != null) && (limitStr.length() > 0)) {
            lmt = Integer.parseInt(limitStr);
        }

        int pg = 1;
        String pageStr = req.queryParams("page");
        if ((pageStr != null) && (pageStr.length() > 0)) {
            pg = Integer.parseInt(pageStr);
        }
        // the skips go from 0 onwards.
        --pg;

        BasicDBObject match = null;

        String scode = req.queryParams("scode");
        if ((scode != null) && (scode.length() > 0)) {
            // this is to get the aggregated list of suppliers
            match = new BasicDBObject("$match", new BasicDBObject("supplier.scode", scode));
        }

        //db.product_supplier_map.aggregate([ {$match:{"supplier.scode":"SP1"}},
        // {$group: {_id:{pcode:"$pcode", pname:"$pname"} }} ])

        Map<String, Object> dbObjIdMap = new HashMap<String, Object>();
        dbObjIdMap.put("pcode", "$pcode");
        dbObjIdMap.put("pname", "$pname");
        dbObjIdMap.put("purl", "$purl");
        DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject(dbObjIdMap));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject limit = new BasicDBObject("$limit", lmt);
        DBObject skip = new BasicDBObject("$skip", pg * lmt);

        List<DBObject> pipeline = Arrays.asList(match, group, skip, limit);

        AggregationOutput aggOutput = prodSupMapCollection_.aggregate(pipeline);
        output = aggOutput.results();

        return output;
    }

    private BasicDBObject prepareProductFields() {
        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 1);
        fields.put("category", 1);
        fields.put("code", 1);
        fields.put("desc", 1);
        fields.put("manufacturers", 1);
        fields.put("name", 1);
        fields.put("purpose", 1);
        fields.put("specs", 1);
        fields.put("starSuppliers", 1);
        fields.put("url", 1);
        fields.put("orderSpec", 1);

        return fields;
    }
}
