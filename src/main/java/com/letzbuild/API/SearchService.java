package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.google.gson.Gson;
import com.mongodb.*;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import spark.Request;

import javax.xml.soap.Node;
import java.util.*;

public class SearchService {

    private DBCollection productsCollection_;
    private DBCollection suppliersCollection_;
    private Properties p_;
    private TransportClient client_;

    public SearchService(final DB letzbuildDB, final Properties p) {
        productsCollection_ = letzbuildDB.getCollection("products");
        suppliersCollection_ = letzbuildDB.getCollection("suppliers");
        p_ = p;

        String ip = p_.getProperty("elasticsearchIP");
        int port = Integer.parseInt(p_.getProperty("elasticsearchPort"));
        String clusterName = p_.getProperty("elasticsearchClusterName");

        Settings settings = ImmutableSettings.settingsBuilder()
                .put("cluster.name", clusterName)
                .build();
        client_ = new TransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(ip, port));
    }

    private TransportClient getConnection() {
        return client_;
    }

    public String productSearch(Request req) {

        String out = "";

        int limit = Integer.parseInt(p_.getProperty("pageLimit"));
        String word = req.queryParams("word");

        SearchRequestBuilder srb = getConnection().prepareSearch("products")
                .setTypes("product")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .addAggregation(AggregationBuilders.terms("catAgg").field("category"))
                .setQuery(QueryBuilders.matchQuery("_all", word)) // Query
                .setFrom(0)
                .setSize(limit);

        MultiSearchResponse sr = getConnection().prepareMultiSearch()
                .add(srb)
                .execute().actionGet();

        for (MultiSearchResponse.Item item : sr.getResponses()) {
            out = item.getResponse().toString();
            break;
        }

        return out;
    }

    public Map<String, Object> globalSearch(Request req) {

        Map<String, Object> out = null;

        int limit = Integer.parseInt(p_.getProperty("pageLimit"));
        String word = req.queryParams("word");

        SearchRequestBuilder srb1 = getConnection().prepareSearch("products")
                .setTypes("product")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("_all", word)) // Query
                .setFrom(0).setSize(5);

        SearchRequestBuilder srb2 = getConnection().prepareSearch("suppliers")
                .setTypes("supplier")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("_all", word)) // Query
                .setFrom(0).setSize(5);

        MultiSearchResponse sr = getConnection().prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .execute().actionGet();

        // You will get all individual responses from MultiSearchResponse#getResponses()
        out = new HashMap<String, Object>();
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            SearchHit[] output = response.getHits().getHits();

            List<Object> res = new ArrayList<Object>();
            String indexName = "";

            for (SearchHit sh : output) {
                if (indexName.length() == 0) indexName = sh.getIndex();
                res.add(sh.getSource());
            }

            out.put(indexName, res);
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

            // add this product to elasticsearch
            IndexResponse response = getConnection().prepareIndex("products", "product", obj.get("code").toString())
                    .setSource(jsonDocument)
                    .execute()
                    .actionGet();
        }
    }

    public void indexSuppliers() {

        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("name", 1);
        fields.put("rating", 1);
        fields.put("city", 1);
        fields.put("state", 1);
        fields.put("address", 1);
        fields.put("geoLoc", 1);
        fields.put("code", 1);

        DBCursor cursor = suppliersCollection_.find(new BasicDBObject(), fields);
        for (DBObject obj : cursor) {

            Map<String, Object> jsonDocument = new HashMap<>();
            jsonDocument.put("id", obj.get("code"));
            jsonDocument.put("name", obj.get("name"));
            jsonDocument.put("rating", obj.get("rating"));
            jsonDocument.put("city", obj.get("city"));
            jsonDocument.put("state", obj.get("state"));
            jsonDocument.put("address", obj.get("address"));
            jsonDocument.put("geoLoc", obj.get("geoLoc"));

            try {

                // add this product to elasticsearch
                IndexResponse response = getConnection().prepareIndex("suppliers", "supplier", obj.get("code").toString())
                        .setSource(jsonDocument)
                        .execute()
                        .actionGet();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
