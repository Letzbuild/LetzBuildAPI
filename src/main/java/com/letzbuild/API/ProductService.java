package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import spark.Request;

import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class ProductService {

    private DBCollection productsCollection_;
    private Properties p_;

    public ProductService(final DB letzbuildDB, final Properties p) {
        productsCollection_ = letzbuildDB.getCollection("products");
        p_ = p;
    }

    public List<DBObject> searchProducts(Request req) {
        List<DBObject> products = null;

        int limit = Integer.parseInt(p_.getProperty("pageLimit"));
        String limitStr = req.queryParams("limit");
        if ( (limitStr !=  null) && (limitStr.length() > 0) ) {
            limit = Integer.parseInt(req.queryParams("limit"));
        }

        BasicDBObject query = new BasicDBObject();

        String keyword = req.queryParams("word");
        if ( (keyword != null) && (keyword.length() > 0) ) {
            //db.products.find({searchDesc: {$regex:/blue/i}})

            String pattern = ".*\\b" + keyword +"\\b.*";
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            query.append("searchDesc", regex);
        }

        String category = req.queryParams("cat");
        if ( (category != null) && (category.length() > 0) ) {
            //db.products.find({category: "Steel")

            query.append("category", category);
        }

        if (query == null) throw new IllegalArgumentException("One parameter is mandatory and cannot be empty");

        DBCursor cursor = productsCollection_.find(query).limit(limit);
        try {
            products = cursor.toArray();
        } finally {
            cursor.close();
        }

        return products;
    }
}
