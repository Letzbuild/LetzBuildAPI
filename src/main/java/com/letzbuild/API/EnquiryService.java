package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.*;
import spark.Request;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class EnquiryService {

    private DBCollection supplierEnquiriesCollection_;
    private DBCollection supplierEnquiriesQueueCollection_;

    private DBCollection productEnquiriesCollection_;
    private DBCollection productEnquiriesQueueCollection_;

    private DBCollection bomEnquiriesCollection_;
    private DBCollection bomEnquiriesQueueCollection_;

    private DBCollection pmsEnquiriesCollection_;
    private DBCollection pmsEnquiriesQueueCollection_;

    private DBCollection qsEnquiriesCollection_;
    private DBCollection qsEnquiriesQueueCollection_;


    private Properties p_;

    public EnquiryService(final DB letzbuildDB, final Properties p) {
        supplierEnquiriesCollection_ = letzbuildDB.getCollection("supplier_enquiries");
        supplierEnquiriesQueueCollection_ = letzbuildDB.getCollection("supplier_enquiries_queue");

        productEnquiriesCollection_ = letzbuildDB.getCollection("product_enquiries");
        productEnquiriesQueueCollection_ = letzbuildDB.getCollection("product_enquiries_queue");

        bomEnquiriesCollection_ = letzbuildDB.getCollection("bom_enquiries");
        bomEnquiriesQueueCollection_ = letzbuildDB.getCollection("bom_enquiries_queue");

        pmsEnquiriesCollection_ = letzbuildDB.getCollection("pms_enquiries");
        pmsEnquiriesQueueCollection_ = letzbuildDB.getCollection("pms_enquiries_queue");

        qsEnquiriesCollection_ = letzbuildDB.getCollection("qs_enquiries");
        qsEnquiriesQueueCollection_ = letzbuildDB.getCollection("qs_enquiries_queue");

        p_ = p;
    }

    public void sendSupplierEnquiry(Request req, DBObject prodObj, DBObject suppObj) throws ParseException {

        String enqno = req.queryParams("enquirynumber");
        String scode = req.queryParams("scode");
        String sname = req.queryParams("suppliername");
        String pcode = req.queryParams("pcode");
        String fname = req.queryParams("firstname");
        String lname = req.queryParams("lastname");
        String org = req.queryParams("organisation");
        String mobile = req.queryParams("mobilenumber");
        String email = req.queryParams("email");
        String qty = req.queryParams("quantity");
        String orderSpec = req.queryParams("orderspecification");
        String spec = req.queryParams("specification");
        String dim = req.queryParams("dimension");
        String sub = req.queryParams("subject");
        String needDate = req.queryParams("datepicker");
        String budget = req.queryParams("approximatebudget");
        String loc = req.queryParams("deliverylocation");
        String freq = req.queryParams("frequency");
        String reason = req.queryParams("reasonforpurchase");
        String instr = req.queryParams("anyspecialinstruction");

        DBObject doc = new BasicDBObject();
        doc.put("enqno", enqno);
        doc.put("scode", scode);
        doc.put("sname", sname);
        doc.put("surl", suppObj.get("url"));
        doc.put("pcode", pcode);
        doc.put("pname", prodObj.get("name"));
        doc.put("purl", prodObj.get("url"));
        doc.put("fname", fname);
        doc.put("lname", lname);
        doc.put("org", org);
        doc.put("mobile", mobile);
        doc.put("email", email);
        doc.put("qty", qty);
        doc.put("orderSpec", orderSpec);
        doc.put("spec", spec);
        doc.put("dim", dim);
        doc.put("sub", sub);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        doc.put("needDate", formatter.parse(needDate));
        doc.put("budget", budget);
        doc.put("loc", loc);
        doc.put("freq", freq);
        doc.put("reason", reason);
        doc.put("instr", instr);
        doc.put("enqDate", new Date());

        supplierEnquiriesCollection_.insert(doc);

        DBObject queueDoc = new BasicDBObject("enqno", enqno);
        supplierEnquiriesQueueCollection_.insert(queueDoc);
    }

    public Map<String, Object> retrieveSupplierEnquiries(Request req) {
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

        BasicDBObject query = new BasicDBObject();

        String scode = req.queryParams("scode");
        if ((scode != null) && (scode.length() > 0)) {
            //db.product_enquiries.find({scode: "SP1"})

            query.append("scode", scode);
        }

        long count = supplierEnquiriesCollection_.count(query);
        DBCursor cursor = supplierEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            out = new HashMap<String, Object>();

            out.put("pagination", JsonUtil.constructPageObject(count, page, limit));
            out.put("result", cursor.toArray());
        } finally {
            cursor.close();
        }

        return out;
    }

    public void sendProductEnquiry(Request req, DBObject prodObj) throws ParseException {

        String enqno = req.queryParams("enquirynumber");
        String pcode = req.queryParams("pcode");
        String fname = req.queryParams("firstname");
        String lname = req.queryParams("lastname");
        String org = req.queryParams("organisation");
        String mobile = req.queryParams("mobilenumber");
        String email = req.queryParams("email");
        String qty = req.queryParams("quantity");
        String orderSpec = req.queryParams("orderspecification");
        String spec = req.queryParams("specification");
        String dim = req.queryParams("dimension");
        String sub = req.queryParams("subject");
        String needDate = req.queryParams("datepicker");
        String budget = req.queryParams("approximatebudget");
        String loc = req.queryParams("deliverylocation");
        String freq = req.queryParams("frequency");
        String reason = req.queryParams("reasonforpurchase");
        String instr = req.queryParams("anyspecialinstruction");

        DBObject doc = new BasicDBObject();
        doc.put("enqno", enqno);
        doc.put("pcode", pcode);
        doc.put("pname", prodObj.get("name"));
        doc.put("purl", prodObj.get("url"));
        doc.put("fname", fname);
        doc.put("lname", lname);
        doc.put("org", org);
        doc.put("mobile", mobile);
        doc.put("email", email);
        doc.put("qty", qty);
        doc.put("orderSpec", orderSpec);
        doc.put("spec", spec);
        doc.put("dim", dim);
        doc.put("sub", sub);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        doc.put("needDate", formatter.parse(needDate));
        doc.put("budget", budget);
        doc.put("loc", loc);
        doc.put("freq", freq);
        doc.put("reason", reason);
        doc.put("instr", instr);
        doc.put("enqDate", new Date());

        productEnquiriesCollection_.insert(doc);

        DBObject queueDoc = new BasicDBObject("enqno", enqno);
        productEnquiriesQueueCollection_.insert(queueDoc);
    }

    public Map<String, Object> retrieveProductEnquiries(Request req) {
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

        BasicDBObject query = new BasicDBObject();

        String pcode = req.queryParams("pcode");
        if ((pcode != null) && (pcode.length() > 0)) {
            //db.product_enquiries.find({pcode: "LB1"})

            query.append("pcode", pcode);
        }

        long count = productEnquiriesCollection_.count(query);
        DBCursor cursor = productEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            out = new HashMap<String, Object>();

            out.put("pagination", JsonUtil.constructPageObject(count, page, limit));
            out.put("result", cursor.toArray());
        } finally {
            cursor.close();
        }

        return out;
    }

    public void sendBOMEnquiry(Request req) throws ParseException {

        String enqno = req.queryParams("enquirynumber");
        String fname = req.queryParams("firstname");
        String lname = req.queryParams("lastname");
        String org = req.queryParams("organisation");
        String mobile = req.queryParams("mobilenumber");
        String email = req.queryParams("email");
        String sub = req.queryParams("enquiryheading");
        String loc = req.queryParams("location");
        String instr = req.queryParams("anyadditionalinstruction");

        DBObject doc = new BasicDBObject();
        doc.put("enqno", enqno);
        doc.put("fname", fname);
        doc.put("lname", lname);
        doc.put("org", org);
        doc.put("mobile", mobile);
        doc.put("email", email);
        doc.put("sub", sub);
        doc.put("loc", loc);
        doc.put("instr", instr);
        doc.put("enqDate", new Date());

        bomEnquiriesCollection_.insert(doc);

        DBObject queueDoc = new BasicDBObject("enqno", enqno);
        bomEnquiriesQueueCollection_.insert(queueDoc);
    }

    public Map<String, Object> retrieveBOMEnquiries(Request req) {
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

        BasicDBObject query = new BasicDBObject();

        long count = bomEnquiriesCollection_.count(query);
        DBCursor cursor = bomEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            out = new HashMap<String, Object>();

            out.put("pagination", JsonUtil.constructPageObject(count, page, limit));
            out.put("result", cursor.toArray());
        } finally {
            cursor.close();
        }

        return out;
    }

    public void sendPMSEnquiry(Request req) throws ParseException {

        String enqno = req.queryParams("enquirynumber");
        String fname = req.queryParams("firstname");
        String lname = req.queryParams("lastname");
        String org = req.queryParams("organisation");
        String mobile = req.queryParams("mobilenumber");
        String email = req.queryParams("email");
        String sub = req.queryParams("enquiryheading");
        String loc = req.queryParams("location");
        String instr = req.queryParams("anyadditionalinstruction");

        DBObject doc = new BasicDBObject();
        doc.put("enqno", enqno);
        doc.put("fname", fname);
        doc.put("lname", lname);
        doc.put("org", org);
        doc.put("mobile", mobile);
        doc.put("email", email);
        doc.put("sub", sub);
        doc.put("loc", loc);
        doc.put("instr", instr);
        doc.put("enqDate", new Date());

        pmsEnquiriesCollection_.insert(doc);

        DBObject queueDoc = new BasicDBObject("enqno", enqno);
        pmsEnquiriesQueueCollection_.insert(queueDoc);
    }

    public Map<String, Object> retrievePMSEnquiries(Request req) {
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

        BasicDBObject query = new BasicDBObject();

        long count = pmsEnquiriesCollection_.count(query);
        DBCursor cursor = pmsEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            out = new HashMap<String, Object>();

            out.put("pagination", JsonUtil.constructPageObject(count, page, limit));
            out.put("result", cursor.toArray());
        } finally {
            cursor.close();
        }

        return out;
    }

    public void sendQSEnquiry(Request req) throws ParseException {

        String enqno = req.queryParams("enquirynumber");
        String fname = req.queryParams("firstname");
        String lname = req.queryParams("lastname");
        String org = req.queryParams("organisation");
        String mobile = req.queryParams("mobilenumber");
        String email = req.queryParams("email");
        String sub = req.queryParams("enquiryheading");
        String loc = req.queryParams("location");
        String instr = req.queryParams("anyadditionalinstruction");

        DBObject doc = new BasicDBObject();
        doc.put("enqno", enqno);
        doc.put("fname", fname);
        doc.put("lname", lname);
        doc.put("org", org);
        doc.put("mobile", mobile);
        doc.put("email", email);
        doc.put("sub", sub);
        doc.put("loc", loc);
        doc.put("instr", instr);
        doc.put("enqDate", new Date());

        qsEnquiriesCollection_.insert(doc);

        DBObject queueDoc = new BasicDBObject("enqno", enqno);
        qsEnquiriesQueueCollection_.insert(queueDoc);
    }

    public Map<String, Object> retrieveQSEnquiries(Request req) {
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

        BasicDBObject query = new BasicDBObject();

        long count = qsEnquiriesCollection_.count(query);
        DBCursor cursor = qsEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
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
