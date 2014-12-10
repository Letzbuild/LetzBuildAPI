package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.DBObject;

import java.util.List;

import static com.letzbuild.API.JsonUtil.json;
import static com.letzbuild.API.JsonUtil.toJson;
import static spark.Spark.*;


public class SupplierController {

    public SupplierController(final SupplierService supplierService, UserService userService) {

        // retrieve suppliers given a product code; supports pagination.
        get("/suppliers/retrieve", (req, res) -> {
            // check if it is based on product code.
            Iterable<DBObject> list = supplierService.retrieveSuppliers(req);
            if (list != null) {
                return list;
            }

            res.status(400);
            return new ResponseError("No suppliers results found");
        }, json());

        // retrieve suppliers given a product code; supports pagination.
        get("/suppliers/products/retrieve", (req, res) -> {

            Iterable<DBObject> list = supplierService.retrieveProductsForSupplier(req);
            if (list != null) {
                return list;
            }

            res.status(400);
            return new ResponseError("No suppliers results found");
        }, json());

        get("/suppliers/:scode", (req, res) -> {
            String scode = req.params(":scode");
            DBObject user = supplierService.getDetails(scode);
            if (user != null) {
                return user;
            }
            res.status(400);
            return new ResponseError("failure1");
        }, json());

        post("/suppliers/add", (req, res) -> {
            supplierService.add(req);
            userService.createUser(req.queryParams("email"), req.queryParams("password"), "supplier");
            res.status(201); // 201 Created
            return "Successfully created user";
        }, json());
    }


}