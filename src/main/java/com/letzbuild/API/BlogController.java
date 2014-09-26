package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import static com.letzbuild.API.JsonUtil.json;
import static com.letzbuild.API.JsonUtil.toJson;
import static spark.Spark.*;


public class BlogController {

    public BlogController(final BlogService blogService) {

        get("/blogs", (req, res) -> blogService.getAllPosts(5), json());


        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new ResponseError(e)));
        });
    }
}