package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import static com.letzbuild.API.JsonUtil.json;
import static com.letzbuild.API.JsonUtil.toJson;
import static spark.Spark.*;


public class BuyerController {

    public BuyerController(final BuyerService buyerService, UserService userService) {

        post("/buyers/add", (req, res) -> {
            buyerService.add(req);
            userService.createUser(req.queryParams("email"), req.queryParams("password"), "buyer");
            res.status(201); // 201 Created
            return "Successfully created user";
        }, json());
    }


}