package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.google.gson.Gson;
import com.mongodb.*;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import spark.Request;

import javax.xml.soap.Node;
import java.util.*;

public class SearchService {

    private DBCollection productsCollection_;
    private Properties p_;

    public SearchService(final DB letzbuildDB, final Properties p) {
        productsCollection_ = letzbuildDB.getCollection("products");
        p_ = p;
    }

    private TransportClient getConnection() {
        String ip = p_.getProperty("elasticsearchIP");
        int port = Integer.parseInt(p_.getProperty("elasticsearchPort"));
        String clusterName = p_.getProperty("elasticsearchClusterName");

        System.out.println(ip + "--" + port + "--" + clusterName);

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName)
                .build();
        return new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(ip, port));
    }

    public List<Object> globalSearch(Request req) {

        List<Object> out = null;

        int limit = Integer.parseInt(p_.getProperty("pageLimit"));
        String word = req.queryParams("word");

        SearchResponse response = getConnection().prepareSearch("products")
                .setTypes("product")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("_all", word)) // Query
                .setFrom(0).setSize(limit)
                .execute()
                .actionGet();

        SearchHit[] output = response.getHits().getHits();
        out = new ArrayList<Object>();

        for (SearchHit sh : output) {
            out.add(sh.getSource());
        }

        return out;
    }

    public void indexProducts() {

        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("category", 1);
        fields.put("code", 1);
        fields.put("desc", 1);
        fields.put("manufacturers", 1);
        fields.put("name", 1);
        fields.put("purpose", 1);
        fields.put("url", 1);

        DBCursor cursor = productsCollection_.find(new BasicDBObject(), fields);
        for (DBObject obj : cursor) {

            Map<String, Object> jsonDocument = new HashMap<>();
            jsonDocument.put("id", obj.get("code"));
            jsonDocument.put("category", obj.get("category"));
            jsonDocument.put("code", obj.get("code"));
            jsonDocument.put("desc", obj.get("desc"));
            jsonDocument.put("manufacturers", obj.get("manufacturers"));
            jsonDocument.put("name", obj.get("name"));
            jsonDocument.put("purpose", obj.get("purpose"));
            jsonDocument.put("url", obj.get("url"));

            String doc = obj.toString();
            // add this product to elasticsearch
            IndexResponse response = getConnection().prepareIndex("products", "product", obj.get("code").toString())
                    .setSource(jsonDocument)
                    .execute()
                    .actionGet();

            System.out.println("indexed...");
        }
    }

    public void indexSuppliers() {

        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("category", 1);
        fields.put("code", 1);
        fields.put("desc", 1);
        fields.put("manufacturers", 1);
        fields.put("name", 1);
        fields.put("purpose", 1);
        fields.put("url", 1);

        DBCursor cursor = productsCollection_.find(new BasicDBObject(), fields);
        for (DBObject obj : cursor) {

            Map<String, Object> jsonDocument = new HashMap<>();
            jsonDocument.put("id", obj.get("code"));
            jsonDocument.put("category", obj.get("category"));
            jsonDocument.put("code", obj.get("code"));
            jsonDocument.put("desc", obj.get("desc"));
            jsonDocument.put("manufacturers", obj.get("manufacturers"));
            jsonDocument.put("name", obj.get("name"));
            jsonDocument.put("purpose", obj.get("purpose"));
            jsonDocument.put("url", obj.get("url"));

            String doc = obj.toString();
            // add this product to elasticsearch
            IndexResponse response = getConnection().prepareIndex("products", "product", obj.get("code").toString())
                    .setSource(jsonDocument)
                    .execute()
                    .actionGet();

            System.out.println("indexed...");
        }
    }
}
