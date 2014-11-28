package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import spark.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class EnquiryService {

    private DBCollection productEnquiriesCollection_;
    private DBCollection bomEnquiriesCollection_;
    private DBCollection pmsEnquiriesCollection_;
    private DBCollection qsEnquiriesCollection_;


    private Properties p_;

    public EnquiryService(final DB letzbuildDB, final Properties p) {
        productEnquiriesCollection_ = letzbuildDB.getCollection("product_enquiries");
        bomEnquiriesCollection_ = letzbuildDB.getCollection("bom_enquiries");
        pmsEnquiriesCollection_ = letzbuildDB.getCollection("pms_enquiries");
        qsEnquiriesCollection_ = letzbuildDB.getCollection("qs_enquiries");
        p_ = p;
    }

    public void sendProductEnquiry(Request req) throws ParseException {
        String pcode = req.queryParams("pcode");
        InvalidInputs.failIfInvalid("pcode", pcode);

        String firstname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", firstname);

        String lastname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lastname);

        String organisation = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", organisation);

        String mobilenumber = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobilenumber);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String qty = req.queryParams("qty");
        InvalidInputs.failIfInvalid("qty", qty);

        String orderSpec = req.queryParams("orderSpec");
        InvalidInputs.failIfInvalid("orderSpec", qty);

        String enquiryheading = req.queryParams("enquiryheading");
        InvalidInputs.failIfInvalid("enquiryheading", enquiryheading);

        String needDate = req.queryParams("needDate");
        InvalidInputs.failIfInvalid("needDate", needDate);

        String budget = req.queryParams("budget");
        InvalidInputs.failIfInvalid("budget", budget);

        String location = req.queryParams("location");
        InvalidInputs.failIfInvalid("location", location);

        String freq = req.queryParams("freq");
        InvalidInputs.failIfInvalid("freq", freq);

        String reason = req.queryParams("reason");
        InvalidInputs.failIfInvalid("reason", reason);

        String anyadditionalinstruction = req.queryParams("anyadditionalinstruction");
        InvalidInputs.failIfInvalid("anyadditionalinstruction", anyadditionalinstruction);

        DBObject doc = new BasicDBObject();
        doc.put("pcode", pcode);
        doc.put("firstname", firstname);
        doc.put("lastname", lastname);
        doc.put("organisation", organisation);
        doc.put("mobilenumber", mobilenumber);
        doc.put("email", email);
        doc.put("qty", qty);
        doc.put("orderSpec", orderSpec);
        doc.put("enquiryheading", enquiryheading);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        doc.put("needDate", formatter.parse(needDate));
        doc.put("budget", budget);
        doc.put("location", location);
        doc.put("freq", freq);
        doc.put("reason", reason);
        doc.put("anyadditionalinstruction", anyadditionalinstruction);
        doc.put("enqDate", new Date());

        productEnquiriesCollection_.insert(doc);
    }

    public List<DBObject> retrieveProductEnquiries(Request req) {
        List<DBObject> enquiries = null;

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
            //db.product_enquiries.find({pcode: "LB1"})

            query.append("pcode", pcode);
        }

        DBCursor cursor = productEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            enquiries = cursor.toArray();
        } finally {
            cursor.close();
        }

        return enquiries;
    }

    public void sendBOMEnquiry(Request req) throws ParseException {

        String firstname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", firstname);

        String lastname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lastname);

        String organisation = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", organisation);

        String mobilenumber = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobilenumber);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String enquiryheading = req.queryParams("enquiryheading");
        InvalidInputs.failIfInvalid("enquiryheading", enquiryheading);

        String location = req.queryParams("location");
        InvalidInputs.failIfInvalid("location", location);

        String anyadditionalinstruction = req.queryParams("anyadditionalinstruction");
        InvalidInputs.failIfInvalid("anyadditionalinstruction", anyadditionalinstruction);

        DBObject doc = new BasicDBObject();
        doc.put("firstname", firstname);
        doc.put("lastname", lastname);
        doc.put("organisation", organisation);
        doc.put("mobilenumber", mobilenumber);
        doc.put("email", email);
        doc.put("enquiryheading", enquiryheading);
        doc.put("location", location);
        doc.put("anyadditionalinstruction", anyadditionalinstruction);
        doc.put("enqDate", new Date());

        bomEnquiriesCollection_.insert(doc);
    }

    public List<DBObject> retrieveBOMEnquiries(Request req) {
        List<DBObject> enquiries = null;

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

        DBCursor cursor = bomEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            enquiries = cursor.toArray();
        } finally {
            cursor.close();
        }

        return enquiries;
    }

    public void sendPMSEnquiry(Request req) throws ParseException {

        String firstname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", firstname);

        String lastname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lastname);

        String organisation = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", organisation);

        String mobilenumber = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobilenumber);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String enquiryheading = req.queryParams("enquiryheading");
        InvalidInputs.failIfInvalid("enquiryheading", enquiryheading);

        String location = req.queryParams("location");
        InvalidInputs.failIfInvalid("location", location);

        String anyadditionalinstruction = req.queryParams("anyadditionalinstruction");
        InvalidInputs.failIfInvalid("anyadditionalinstruction", anyadditionalinstruction);

        DBObject doc = new BasicDBObject();
        doc.put("firstname", firstname);
        doc.put("lastname", lastname);
        doc.put("organisation", organisation);
        doc.put("mobilenumber", mobilenumber);
        doc.put("email", email);
        doc.put("enquiryheading", enquiryheading);
        doc.put("location", location);
        doc.put("anyadditionalinstruction", anyadditionalinstruction);
        doc.put("enqDate", new Date());

        pmsEnquiriesCollection_.insert(doc);
    }

    public List<DBObject> retrievePMSEnquiries(Request req) {
        List<DBObject> enquiries = null;

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

        DBCursor cursor = pmsEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            enquiries = cursor.toArray();
        } finally {
            cursor.close();
        }

        return enquiries;
    }

    public void sendQSEnquiry(Request req) throws ParseException {

        String firstname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", firstname);

        String lastname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lastname);

        String organisation = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", organisation);

        String mobilenumber = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobilenumber);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String enquiryheading = req.queryParams("enquiryheading");
        InvalidInputs.failIfInvalid("enquiryheading", enquiryheading);

        String location = req.queryParams("location");
        InvalidInputs.failIfInvalid("location", location);

        String anyadditionalinstruction = req.queryParams("anyadditionalinstruction");
        InvalidInputs.failIfInvalid("anyadditionalinstruction", anyadditionalinstruction);

        DBObject doc = new BasicDBObject();
        doc.put("firstname", firstname);
        doc.put("lastname", lastname);
        doc.put("organisation", organisation);
        doc.put("mobilenumber", mobilenumber);
        doc.put("email", email);
        doc.put("enquiryheading", enquiryheading);
        doc.put("location", location);
        doc.put("anyadditionalinstruction", anyadditionalinstruction);
        doc.put("enqDate", new Date());


        qsEnquiriesCollection_.insert(doc);
    }

    public List<DBObject> retrieveQSEnquiries(Request req) {
        List<DBObject> enquiries = null;

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

        DBCursor cursor = qsEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            enquiries = cursor.toArray();
        } finally {
            cursor.close();
        }

        return enquiries;
    }
}
