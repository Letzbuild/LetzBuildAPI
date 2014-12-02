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
                user.put("message", "success");
                return user;
            }
            res.status(400);
            return new ResponseError("failure");
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
    }
}