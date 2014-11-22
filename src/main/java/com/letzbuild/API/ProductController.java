package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.DBObject;

import java.util.List;

import static com.letzbuild.API.JsonUtil.json;
import static com.letzbuild.API.JsonUtil.toJson;
import static spark.Spark.*;


public class ProductController {

    public ProductController(final ProductService productService) {

        // keyword based search. This is using regex now. Will alter to eleastic search later
        get("/products/search", (req, res) -> {
            if (req.queryParams().size() == 0) {
                return new ResponseError("One of the parameters is mandatory");
            }
            List<DBObject> list = productService.searchProducts(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No products results found");
        }, json());

        // retrive products category wise
        get("/products/retrieve", (req, res) -> {
            if (req.queryParams().size() == 0) {
                return new ResponseError("One of the parameters is mandatory");
            }
            List<DBObject> list = productService.retrieveProducts(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No products results found");
        }, json());

        // get subcategories for a specified category
        get("/products/categories/:category", (req, res) -> {
            String category = req.params(":category");
            List<DBObject> list = productService.retrieveCategories(category);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No product category results found");
        }, json());

        // get all categories that are at root level i.e. parent is null
        get("/products/categories", (req, res) -> {
            List<DBObject> list = productService.retrieveCategories();
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No product categories found");
        }, json());
    }
}