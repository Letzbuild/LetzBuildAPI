package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import spark.Request;

import java.util.*;
import java.util.regex.Pattern;

public class ProductService {

    private DBCollection productsCollection_;
    private DBCollection categoriesCollection_;

    private Properties p_;

    public ProductService(final DB letzbuildDB, final Properties p) {
        productsCollection_ = letzbuildDB.getCollection("products");
        categoriesCollection_ = letzbuildDB.getCollection("categories");
        p_ = p;
    }

    public Map<String, Object> retrieveCategories(Request req) {
        Map<String, Object> out = null;

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

        //db.categories.find({parent: null})
        BasicDBObject query = new BasicDBObject();
        query.append("parent", null);

        long count = categoriesCollection_.count(query);
        DBCursor cursor = categoriesCollection_.find(query).skip(page * limit).limit(limit);

        try {
            out = new HashMap<String, Object>();

            out.put("pagination", JsonUtil.constructPageObject(count, page, limit));
            out.put("result", cursor.toArray());
        } finally {
            cursor.close();
        }

        return out;
    }

    public List<DBObject> retrieveCategories(String category) {
        List<DBObject> categories = null;

        BasicDBObject query = new BasicDBObject();

        query.append("category", category);


        DBCursor cursor = categoriesCollection_.find(query);
        try {
            categories = cursor.toArray();
        } finally {
            cursor.close();
        }

        return categories;
    }

    public Map<String, Object> retrieveProducts(Request req) {
        Map<String, Object> out = null;

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

        String pcode = req.queryParams("pcode");
        if ((pcode != null) && (pcode.length() > 0)) {
            //db.products.find({category: "Steel"})

            query.append("code", pcode);
        }

        long count = productsCollection_.count(query);
        DBCursor cursor = productsCollection_.find(query, prepareProductFields()).skip(page * limit).limit(limit);

        try {
            out = new HashMap<String, Object>();
            out.put("pagination", JsonUtil.constructPageObject(count, page, limit));
            out.put("result", cursor.toArray());
        } finally {
            cursor.close();
        }

        return out;
    }

    private BasicDBObject prepareProductFields() {
        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 1);
        fields.put("category", 1);
        fields.put("code", 1);
        fields.put("manufacturers", 1);
        fields.put("name", 1);
        fields.put("purpose", 1);
        fields.put("dim", 1);
        fields.put("specs", 1);
        fields.put("url", 1);
        fields.put("orderSpec", 1);

        return fields;
    }
}
