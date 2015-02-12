package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import spark.Request;

import java.util.*;

public class SupplierService {

    private DBCollection suppliersCollection_;
    private DBCollection prodSupMapCollection_;
    private DB letzbuildDB_;

    private Properties p_;

    public SupplierService(final DB letzbuildDB, final Properties p) {
        letzbuildDB_ = letzbuildDB;
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
    public Iterable<DBObject> retrieveSuppliersBasedOnCategory(Request req) {
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

        // this is to get the aggregated list of suppliers
        BasicDBObject match = new BasicDBObject("$match", new BasicDBObject("category", category));

        //db.product_supplier_map.aggregate([ {$match:{category:"Sand"}},
        // {$group: {_id:{scode:"$supplier.scode", sname:"$supplier.name"} }} ])

        Map<String, Object> dbObjIdMap = new HashMap<String, Object>();
        dbObjIdMap.put("scode", "$supplier.scode");
        dbObjIdMap.put("sname", "$supplier.name");
        dbObjIdMap.put("rating", "$supplier.rating");

        DBObject groupFields = new BasicDBObject( "_id", new BasicDBObject(dbObjIdMap));
        DBObject group = new BasicDBObject("$group", groupFields);

        DBObject limit = new BasicDBObject("$limit", lmt);
        DBObject skip = new BasicDBObject("$skip", pg * lmt);

        List<DBObject> pipeline = Arrays.asList(match, group, skip, limit);

        AggregationOutput aggOutput = prodSupMapCollection_.aggregate(pipeline);
        output = aggOutput.results();

        // we need to get the products that belong to this category that the
        // supplier is supplying and attach....
        for (DBObject obj : output) {
            BasicDBObject dbo = (BasicDBObject)obj.get("_id");
            String scode = dbo.get("scode").toString();

            // this is where we stuff the products for a supplier filtered by category
            Iterable<DBObject> prods = retrieveProductsForSupplier(category, scode, 0, 10);
            dbo.put("prods", prods);
        }

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

    // retrieve the suppliers based on a product code.
    public Map<String, Object> retrieveSuppliersBasedOnProduct(Request req) {
        Map<String, Object> out = null;

        String pcode = req.queryParams("pcode");

        // this is to get the aggregated list of suppliers
        BasicDBObject query = new BasicDBObject("pcode", pcode);

        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("pcode", 1);
        fields.put("pname", 1);
        fields.put("purl", 1);
        fields.put("supplier", 1);

        BasicDBObject sort = new BasicDBObject("supplier.rating", -1);

        out = JsonUtil.constructPaginatedOut(p_, req, query, prodSupMapCollection_, fields, sort);

        List<DBObject> list = (List<DBObject>)out.get("result");

        // cycle through this list and add more details to the supplier.
        for (DBObject sobj : list) {

            BasicDBObject supp = (BasicDBObject)sobj.get("supplier");
            DBObject suppObj = suppliersCollection_.findOne(new BasicDBObject("code", supp.get("scode")));

            supp.put("address", suppObj.get("address"));
            supp.put("contact", suppObj.get("contact"));
            supp.put("address", suppObj.get("address"));
            supp.put("email", suppObj.get("email"));
            supp.put("phone", suppObj.get("phone"));
            supp.put("url", suppObj.get("url"));
        }

        return out;
    }

    // retrieve the suppliers based on a product code.
    public BasicDBObject retrieveSuppliersBasedOnProductAndCoords(Request req) {
        BasicDBObject out = null;

        String pcode = req.queryParams("pcode");

        // this is to get the aggregated list of suppliers
        BasicDBObject query = new BasicDBObject("pcode", pcode);

        String lat = req.queryParams("lat");
        String lon = req.queryParams("lon");

        int limit = Integer.parseInt(p_.getProperty("pageLimit"));
        String limitStr = req.queryParams("limit");
        if ((limitStr != null) && (limitStr.length() > 0)) {
            limit = Integer.parseInt(limitStr);
        }

        BasicDBObject myCmd = new BasicDBObject();
        myCmd.append("geoNear", "product_supplier_map");
        double[] loc = {Double.parseDouble(lon), Double.parseDouble(lat)};
        BasicDBObject near = new BasicDBObject("type", "Point").append("coordinates", loc);
        myCmd.append("near", near);
        myCmd.append("spherical", true);
        myCmd.append("distanceMultiplier", (double) 0.001);
        myCmd.append("num", limit);

        myCmd.append("query", query);

        CommandResult myResult = letzbuildDB_.command(myCmd);
        List<DBObject> list = (List<DBObject>) myResult.get("results");

        List<BasicDBObject> outList = new ArrayList<BasicDBObject>();

        // cycle through this list and add more details to the supplier.
        for (DBObject obj : list) {

            BasicDBObject orig = (BasicDBObject) obj.get("obj");

            BasicDBObject supp = (BasicDBObject)orig.get("supplier");
            DBObject suppObj = suppliersCollection_.findOne(new BasicDBObject("code", supp.get("scode")));

            BasicDBObject supplier = new BasicDBObject();
            supplier.put("address", suppObj.get("address"));
            supplier.put("contact", suppObj.get("contact"));
            supplier.put("address", suppObj.get("address"));
            supplier.put("email", suppObj.get("email"));
            supplier.put("phone", suppObj.get("phone"));
            supplier.put("url", suppObj.get("url"));
            supplier.put("name", suppObj.get("name"));
            supplier.put("scode", suppObj.get("code"));
            supplier.put("rating", suppObj.get("rating"));
            supplier.put("distance", obj.get("dis"));

            BasicDBObject doc = new BasicDBObject();
            doc.put("pcode", orig.get("pcode"));
            doc.put("pname", orig.get("pname"));
            doc.put("purl", orig.get("purl"));
            doc.put("supplier", supplier);

            outList.add(doc);
        }

        out = new BasicDBObject("result", outList);

        return out;
    }

    public DBObject retrieveSupplier(String code) {
        DBObject out = null;

        BasicDBObject query = new BasicDBObject("code", code);
        out = suppliersCollection_.findOne(query);

        return out;
    }


    public Map<String, Object> retrieveProductsForSupplier(Request req) {
        Map<String, Object> out = null;

        int limit = Integer.parseInt(p_.getProperty("pageLimit"));
        String limitStr = req.queryParams("limit");
        if ((limitStr != null) && (limitStr.length() > 0)) {
            limit = Integer.parseInt(limitStr);
        }

        int page = 1;
        String pageStr = req.queryParams("page");
        if ((pageStr != null) && (pageStr.length() > 0)) {
            page = Integer.parseInt(pageStr);
        }
        // the skips go from 0 onwards.
        --page;

        String scode = req.queryParams("scode");

        // this is to get the aggregated list of suppliers
        BasicDBObject query = new BasicDBObject("supplier.scode", scode);

        BasicDBObject fields = new BasicDBObject();
        fields.put("_id", 0);
        fields.put("pcode", 1);
        fields.put("pname", 1);
        fields.put("purl", 1);

        long count = prodSupMapCollection_.count(query);
        DBCursor cursor = prodSupMapCollection_.find(query, fields).skip(page * limit).limit(limit);

        try {
            out = new HashMap<String, Object>();

            out.put("pagination", JsonUtil.constructPageObject(count, page, limit));
            out.put("result", cursor.toArray());
        } finally {
            cursor.close();
        }

        return out;
    }
}
