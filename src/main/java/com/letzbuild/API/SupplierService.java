package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import spark.Request;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

public class SupplierService {

    private DBCollection suppliersCollection_;
    private DBCollection prodSupMapCollection_;

    private Properties p_;

    public SupplierService(final DB letzbuildDB, final Properties p) {
        suppliersCollection_ = letzbuildDB.getCollection("suppliers");
        prodSupMapCollection_ = letzbuildDB.getCollection("product_supplier_map");
        p_ = p;
    }

    public DBObject getDetails(String scode) {
        DBObject user = null;

        user = suppliersCollection_.findOne(new BasicDBObject("code", scode));

        return user;
    }

    public void add(Request req) {
        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String password = req.queryParams("password");
        InvalidInputs.failIfInvalid("password", password);

        String fname = req.queryParams("fname");
        InvalidInputs.failIfInvalid("fname", fname);

        String city = req.queryParams("city");
        InvalidInputs.failIfInvalid("city", city);

        String mobile = req.queryParams("mobile");
        InvalidInputs.failIfInvalid("mobile", mobile);

        String company = req.queryParams("company");
        InvalidInputs.failIfInvalid("company", company);

        DBObject doc = new BasicDBObject();
        doc.put("contact", new BasicDBObject("fname", fname).append("lname", req.queryParams("lname")));
        doc.put("name", company);
        doc.put("city", city);
        doc.put("mobile", mobile);
        doc.put("email", email);
        doc.put("address", req.queryParams("address"));
        doc.put("zip", req.queryParams("zip"));

        suppliersCollection_.insert(doc);

    }

    // retrieve the suppliers based on a product sub category.
    public Iterable<DBObject> retrieveSuppliers(Request req) {
        Iterable<DBObject> output = null;

        int lmt = Integer.parseInt(p_.getProperty("pageLimit"));
        String limitStr = req.queryParams("limit");
        if ((limitStr != null) && (limitStr.length() > 0)) {
            lmt = Integer.parseInt(limitStr);
        }

        int pg = 1;
        String pageStr = req.queryParams("page");
        if ((pageStr != null) && (pageStr.length() > 0)) {
            pg = Integer.parseInt(pageStr);
        }
        // the skips go from 0 onwards.
        --pg;

        BasicDBObject match = null;

        String category = req.queryParams("cat");
        if ((category != null) && (category.length() > 0)) {
            // this is to get the aggregated list of suppliers
            match = new BasicDBObject("$match", new BasicDBObject("category", category));
        }
        String pcode = req.queryParams("pcode");
        if ((pcode != null) && (pcode.length() > 0)) {
            // this is to get the aggregated list of suppliers
            match = new BasicDBObject("$match", new BasicDBObject("pcode", pcode));
        }

        //db.product_supplier_map.aggregate([ {$match:{category:"Sand"}},
        // {$group: {_id:{scode:"$supplier.scode", sname:"$supplier.name"} }} ])

        Map<String, Object> dbObjIdMap = new HashMap<String, Object>();
        dbObjIdMap.put("scode", "$supplier.scode");
        dbObjIdMap.put("sname", "$supplier.name");
        dbObjIdMap.put("pcode", "$pcode");
        dbObjIdMap.put("pname", "$pname");
        dbObjIdMap.put("purl", "$purl");
        DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject(dbObjIdMap));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject limit = new BasicDBObject("$limit", lmt);
        DBObject skip = new BasicDBObject("$skip", pg * lmt);

        List<DBObject> pipeline = Arrays.asList(match, group, skip, limit);

        AggregationOutput aggOutput = prodSupMapCollection_.aggregate(pipeline);
        output = aggOutput.results();

        if ((category != null) && (category.length() > 0)) {
            // we need to get the products that belong to this category that the
            // supplier is supplying and attach....
            for (DBObject obj : output) {
                BasicDBObject dbo = (BasicDBObject)obj.get("_id");
                String scode = dbo.get("scode").toString();

                // this is where we stuff the products for a supplier filtered by category
                Iterable<DBObject> prods = retrieveProductsForSupplier(category, scode, 0, 10);
                dbo.put("prods", prods);
            }
        }

        return output;
    }

    public Iterable<DBObject> retrieveProductsForSupplier(Request req) {
        Iterable<DBObject> output = null;

        int lmt = Integer.parseInt(p_.getProperty("pageLimit"));
        String limitStr = req.queryParams("limit");
        if ((limitStr != null) && (limitStr.length() > 0)) {
            lmt = Integer.parseInt(limitStr);
        }

        int pg = 1;
        String pageStr = req.queryParams("page");
        if ((pageStr != null) && (pageStr.length() > 0)) {
            pg = Integer.parseInt(pageStr);
        }
        // the skips go from 0 onwards.
        --pg;

        String category = req.queryParams("cat");
        String scode = req.queryParams("scode");

        output = retrieveProductsForSupplier(category, scode, pg, lmt);

        return output;
    }

    public Iterable<DBObject> retrieveProductsForSupplier(String cat, String scode, int pg, int lmt) {
        Iterable<DBObject> output = null;

        BasicDBObject match = null;

        if ((scode != null) && (scode.length() > 0)) {
            // this is to get the aggregated list of suppliers

            BasicDBObject matchCond = new BasicDBObject("supplier.scode", scode);

            if ((cat != null) && (cat.length() > 0)) {
                matchCond.append("category", cat);
            }

            match = new BasicDBObject("$match", matchCond);
        }

        //db.product_supplier_map.aggregate([ {$match:{"supplier.scode":"SP1"}},
        // {$group: {_id:{pcode:"$pcode", pname:"$pname"} }} ])

        Map<String, Object> dbObjIdMap = new HashMap<String, Object>();
        dbObjIdMap.put("pcode", "$pcode");
        dbObjIdMap.put("pname", "$pname");
        dbObjIdMap.put("purl", "$purl");
        DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject(dbObjIdMap));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject limit = new BasicDBObject("$limit", lmt); // hardcoded
        DBObject skip = new BasicDBObject("$skip", pg * lmt); // hardcoded

        List<DBObject> pipeline = Arrays.asList(match, group, skip, limit);

        AggregationOutput aggOutput = prodSupMapCollection_.aggregate(pipeline);
        output = aggOutput.results();

        return output;
    }
}
