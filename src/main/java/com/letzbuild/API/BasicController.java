package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.DBObject;

import static com.letzbuild.API.JsonUtil.json;
import static com.letzbuild.API.JsonUtil.toJson;
import static spark.Spark.*;


public class BasicController {

    public BasicController() {

        after((req, res) -> {
            res.type("application/json");
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Request-Method", "*");
            res.header("Access-Control-Allow-Headers", "*");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new ResponseError(e)));
        });
    }
}