package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.DBObject;
import org.elasticsearch.search.SearchHit;

import java.util.List;
import java.util.Map;

import static com.letzbuild.API.JsonUtil.json;
import static spark.Spark.get;


public class SearchController {

    public SearchController(final SearchService searchService) {

        // http://url:port/search/global?&word=word
        get("/search/global", (req, res) -> {
            if (req.queryParams().size() == 0) {
                return new ResponseError("One of the parameters is mandatory");
            }
            Map<String, Object> list = searchService.globalSearch(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No results found");
        }, json());

        // http://url:port/search/indexproducts
        get("/search/indexproducts", (req, res) -> {
            searchService.indexProducts();

            res.status(201); // 201 Created
            return "success";
        }, json());

        // http://url:port/search/indexsuppliers
        get("/search/indexsuppliers", (req, res) -> {
            searchService.indexSuppliers();

            res.status(201); // 201 Created
            return "success";
        }, json());

    }
}