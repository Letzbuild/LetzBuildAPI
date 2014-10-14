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


        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new ResponseError(e)));
        });
    }
}