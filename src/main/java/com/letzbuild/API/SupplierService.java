package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import spark.Request;

import java.util.List;
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

    public List<DBObject> retrieveSuppliers(Request req) {
        List<DBObject> suppliers = null;

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

        BasicDBObject query = new BasicDBObject();

        String pcode = req.queryParams("pcode");
        if ((pcode != null) && (pcode.length() > 0)) {
            //db.product_supplier_map.find({pcode: "LB123"})

            query.append("pcode", pcode);
        }

        DBCursor cursor = prodSupMapCollection_.find(query).skip(page * limit).limit(limit);
        try {
            suppliers = cursor.toArray();
        } finally {
            cursor.close();
        }

        return suppliers;
    }
}
