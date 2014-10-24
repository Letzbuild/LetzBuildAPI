package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */
import com.mongodb.DBObject;

import static spark.Spark.*;
import static com.letzbuild.API.JsonUtil.*;


public class UserController {

    public UserController(final UserService userService) {

        get("/users/:email", (req, res) -> {
            String email = req.params(":email");
            DBObject user = userService.getUser(email);
            if (user != null) {
                return user;
            }
            res.status(400);
            return new ResponseError("No user with email '%s' found", email);
        }, json());

        /*post("/users", (req, res) -> userService.createUser(
                req.queryParams("name"),
                req.queryParams("email")
        ), json());

        put("/users/:id", (req, res) -> userService.updateUser(
                req.params(":id"),
                req.queryParams("name"),
                req.queryParams("email")
        ), json());*/

        after((req, res) -> {
            res.type("application/json");
        });

        exception(IllegalArgumentException.class, (e, req, res) -> {
            res.status(400);
            res.body(toJson(new ResponseError(e)));
        });
    }
}