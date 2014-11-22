package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import spark.Request;

public class SupplierService {

    private DBCollection suppliersCollection_;

    public SupplierService(final DB letzbuildDB) {
        suppliersCollection_ = letzbuildDB.getCollection("suppliers");
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
}
