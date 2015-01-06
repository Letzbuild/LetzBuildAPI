package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.DBObject;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.search.SearchHit;

import java.util.List;
import java.util.Map;

import static com.letzbuild.API.JsonUtil.json;
import static com.letzbuild.API.JsonUtil.toJson;
import static spark.Spark.*;


public class ProductController {

    public ProductController(final ProductService productService) {

        // http://url:port/products/retrieve?cat=full_word&pcode=product_code&limit=num&page=num
        get("/products/retrieve", (req, res) -> {
            if (req.queryParams().size() == 0) {
                return new ResponseError("One of the parameters is mandatory");
            }

            Map<String, Object> list = productService.retrieveProducts(req);
            if (list != null) {
                return list;
            }

            res.status(400);
            return new ResponseError("No products results found");
        }, json());

        // http://url:port/products/categories/:category
        get("/products/categories/:category", (req, res) -> {
            String category = req.params(":category");
            List<DBObject> list = productService.retrieveCategories(category);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No product category results found");
        }, json());

        // http://url:port/products/categories
        get("/products/categories", (req, res) -> {
            Map<String, Object> list = productService.retrieveCategories(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No product categories found");
        }, json());
    }
}