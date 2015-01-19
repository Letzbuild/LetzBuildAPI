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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

        //db.categories.find({parent: null})
        BasicDBObject query = new BasicDBObject();
        query.append("parent", null);

        out = JsonUtil.constructPaginatedOut(p_, req, query, categoriesCollection_, null, null);

        return out;
    }

    public Map<String, Object> retrieveSubCategories(Request req)  {
        Map<String, Object> out = null;

        String category = req.params(":category");
        try {
            category = URLDecoder.decode(category, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }

        BasicDBObject query = new BasicDBObject();
        query.append("parent", category);

        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("category", 1);
        fields.put("parent", 1);
        fields.put("cnt", 1);

        out = JsonUtil.constructPaginatedOut(p_, req, query, categoriesCollection_, fields, null);

        return out;
    }


    public Map<String, Object> retrieveProducts(Request req) {
        Map<String, Object> out = null;

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

        out = JsonUtil.constructPaginatedOut(p_, req, query, productsCollection_, fields, null);

        return out;
    }

    public DBObject retrieveProduct(String code) {
        DBObject out = null;

        BasicDBObject query = new BasicDBObject("code", code);
        out = productsCollection_.findOne(query);

        return out;
    }

}
