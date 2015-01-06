package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.DBObject;

import java.util.List;
import java.util.Map;

import static com.letzbuild.API.JsonUtil.json;
import static com.letzbuild.API.JsonUtil.toJson;
import static spark.Spark.*;


public class SupplierController {

    public SupplierController(final SupplierService supplierService, UserService userService) {

        // http://url:port/suppliers/retrieve?pcode=product_code&limit=num&page=num
        get("/suppliers/retrieve", (req, res) -> {
            // check if it is based on product code.
            Map<String, Object> list = supplierService.retrieveSuppliersBasedOnProduct(req);
            if (list != null) {
                return list;
            }

            res.status(400);
            return new ResponseError("No suppliers results found");
        }, json());

        // http://url:port/suppliers/products/retrieve?scode=supplier_code&limit=num&page=num
        get("/suppliers/products/retrieve", (req, res) -> {

            Map<String, Object> list = supplierService.retrieveProductsForSupplier(req);
            if (list != null) {
                return list;
            }

            res.status(400);
            return new ResponseError("No suppliers results found");
        }, json());

        //http://url:port/suppliers/:scode
        get("/suppliers/:scode", (req, res) -> {
            String scode = req.params(":scode");
            DBObject user = supplierService.getDetails(scode);
            if (user != null) {
                return user;
            }
            res.status(400);
            return new ResponseError("failure1");
        }, json());

        //http://url:port/suppliers/add
        post("/suppliers/add", (req, res) -> {
            supplierService.add(req);
            userService.createUser(req.queryParams("email"), req.queryParams("password"), "supplier");
            res.status(201); // 201 Created
            return "Successfully created user";
        }, json());
    }


}