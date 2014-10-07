package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import spark.Request;

import java.util.List;
import java.util.regex.Pattern;

public class ProductService {

    private DBCollection productsCollection;

    public ProductService(final DB letzbuildDB) {
        productsCollection = letzbuildDB.getCollection("products");
    }

    public List<DBObject> searchProducts(Request req, int limit) {
        List<DBObject> products;

        DBObject query = new BasicDBObject();

        String keyword = req.queryParams("word");
        if ( (keyword != null ) && (keyword.length() > 0) ) {
            //db.products.find({ $or: [ { category: {$regex:/blue/i} }, { name: {$regex:/blue/i} } ] })

            String pattern = ".*\\b" + keyword +"\\b.*";
            Pattern regex = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            DBObject categoryClause = new BasicDBObject("category", regex);
            DBObject nameClause = new BasicDBObject("name", regex);

            BasicDBList searchList = new BasicDBList();
            searchList.add(categoryClause);
            searchList.add(nameClause);
            query.put("$or", searchList);
        }

        String purpose = req.queryParams("purp");
        if ( (purpose != null ) && (purpose.length() > 0) ) {
            //db.products.find({purpose:"Beam"})

            query.put("purpose", purpose);
        }

        DBCursor cursor = productsCollection.find(query).limit(limit);
        try {
            products = cursor.toArray();
        } finally {
            cursor.close();
        }
        return products;
    }

    private void failIfInvalid(String name, String email) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'name' cannot be empty");
        }
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Parameter 'email' cannot be empty");
        }
    }
}
