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

    private DBCollection supplierEnquiriesCollection_;
    private DBCollection productEnquiriesCollection_;
    private DBCollection bomEnquiriesCollection_;
    private DBCollection pmsEnquiriesCollection_;
    private DBCollection qsEnquiriesCollection_;


    private Properties p_;

    public EnquiryService(final DB letzbuildDB, final Properties p) {
        supplierEnquiriesCollection_ = letzbuildDB.getCollection("supplier_enquiries");
        productEnquiriesCollection_ = letzbuildDB.getCollection("product_enquiries");
        bomEnquiriesCollection_ = letzbuildDB.getCollection("bom_enquiries");
        pmsEnquiriesCollection_ = letzbuildDB.getCollection("pms_enquiries");
        qsEnquiriesCollection_ = letzbuildDB.getCollection("qs_enquiries");
        p_ = p;
    }

    public void sendSupplierEnquiry(Request req) throws ParseException {

        String enqno = req.queryParams("enquirynumber");
        InvalidInputs.failIfInvalid("enquirynumber", enqno);

        String scode = req.queryParams("scode");
        InvalidInputs.failIfInvalid("scode", scode);

        String sname = req.queryParams("suppliername");
        InvalidInputs.failIfInvalid("suppliername", sname);

        String pcode = req.queryParams("pcode");
        InvalidInputs.failIfInvalid("pcode", pcode);

        String fname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", fname);

        String lname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lname);

        String org = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", org);

        String mobile = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String qty = req.queryParams("quantity");
        InvalidInputs.failIfInvalid("quantity", qty);

        String orderSpec = req.queryParams("specification");
        InvalidInputs.failIfInvalid("specification", orderSpec);

        String sub = req.queryParams("subject");
        InvalidInputs.failIfInvalid("subject", sub);

        String needDate = req.queryParams("datepicker");
        InvalidInputs.failIfInvalid("datepicker", needDate);

        String budget = req.queryParams("approximatebudget");
        InvalidInputs.failIfInvalid("approximatebudget", budget);

        String loc = req.queryParams("deliverylocation");
        InvalidInputs.failIfInvalid("deliverylocation", loc);

        String freq = req.queryParams("frequency");
        InvalidInputs.failIfInvalid("frequency", freq);

        String reason = req.queryParams("reasonforpurchase");
        InvalidInputs.failIfInvalid("reasonforpurchase", reason);

        String instr = req.queryParams("anyspecialinstruction");
        InvalidInputs.failIfInvalid("anyspecialinstruction", instr);

        DBObject doc = new BasicDBObject();
        doc.put("enqno", enqno);
        doc.put("scode", scode);
        doc.put("sname", sname);
        doc.put("pcode", pcode);
        doc.put("fname", fname);
        doc.put("lname", lname);
        doc.put("org", org);
        doc.put("mobile", mobile);
        doc.put("email", email);
        doc.put("qty", qty);
        doc.put("orderSpec", orderSpec);
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
    }

    public List<DBObject> retrieveSupplierEnquiries(Request req) {
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

        String scode = req.queryParams("scode");
        if ((scode != null) && (scode.length() > 0)) {
            //db.product_enquiries.find({scode: "SP1"})

            query.append("scode", scode);
        }

        DBCursor cursor = supplierEnquiriesCollection_.find(query).skip(page * limit)
                .sort(new BasicDBObject("enqDate", -1)).limit(limit);
        try {
            enquiries = cursor.toArray();
        } finally {
            cursor.close();
        }

        return enquiries;
    }

    public void sendProductEnquiry(Request req) throws ParseException {

        String enqno = req.queryParams("enquirynumber");
        InvalidInputs.failIfInvalid("enquirynumber", enqno);

        String pcode = req.queryParams("pcode");
        InvalidInputs.failIfInvalid("pcode", pcode);

        String fname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", fname);

        String lname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lname);

        String org = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", org);

        String mobile = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String qty = req.queryParams("quantity");
        InvalidInputs.failIfInvalid("quantity", qty);

        String orderSpec = req.queryParams("orderspecification");
        InvalidInputs.failIfInvalid("orderspecification", orderSpec);

        String spec = req.queryParams("specification");
        InvalidInputs.failIfInvalid("specification", spec);

        String dim = req.queryParams("dimension");
        InvalidInputs.failIfInvalid("dimension", dim);

        String sub = req.queryParams("subject");
        InvalidInputs.failIfInvalid("subject", sub);

        String needDate = req.queryParams("datepicker");
        InvalidInputs.failIfInvalid("datepicker", needDate);

        String budget = req.queryParams("approximatebudget");
        InvalidInputs.failIfInvalid("approximatebudget", budget);

        String loc = req.queryParams("deliverylocation");
        InvalidInputs.failIfInvalid("deliverylocation", loc);

        String freq = req.queryParams("frequency");
        InvalidInputs.failIfInvalid("frequency", freq);

        String reason = req.queryParams("reasonforpurchase");
        InvalidInputs.failIfInvalid("reasonforpurchase", reason);

        String instr = req.queryParams("anyspecialinstruction");
        InvalidInputs.failIfInvalid("anyspecialinstruction", instr);

        DBObject doc = new BasicDBObject();
        doc.put("enqno", enqno);
        doc.put("pcode", pcode);
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

        String enqno = req.queryParams("enquirynumber");
        InvalidInputs.failIfInvalid("enquirynumber", enqno);

        String fname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", fname);

        String lname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lname);

        String org = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", org);

        String mobile = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String sub = req.queryParams("enquiryheading");
        InvalidInputs.failIfInvalid("enquiryheading", sub);

        String loc = req.queryParams("location");
        InvalidInputs.failIfInvalid("location", loc);

        String instr = req.queryParams("anyadditionalinstruction");
        InvalidInputs.failIfInvalid("anyadditionalinstruction", instr);

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

        String enqno = req.queryParams("enquirynumber");
        InvalidInputs.failIfInvalid("enquirynumber", enqno);

        String fname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", fname);

        String lname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lname);

        String org = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", org);

        String mobile = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String sub = req.queryParams("enquiryheading");
        InvalidInputs.failIfInvalid("enquiryheading", sub);

        String loc = req.queryParams("location");
        InvalidInputs.failIfInvalid("location", loc);

        String instr = req.queryParams("anyadditionalinstruction");
        InvalidInputs.failIfInvalid("anyadditionalinstruction", instr);

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

        String enqno = req.queryParams("enquirynumber");
        InvalidInputs.failIfInvalid("enquirynumber", enqno);

        String fname = req.queryParams("firstname");
        InvalidInputs.failIfInvalid("firstname", fname);

        String lname = req.queryParams("lastname");
        InvalidInputs.failIfInvalid("lastname", lname);

        String org = req.queryParams("organisation");
        InvalidInputs.failIfInvalid("organisation", org);

        String mobile = req.queryParams("mobilenumber");
        InvalidInputs.failIfInvalid("mobilenumber", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String sub = req.queryParams("enquiryheading");
        InvalidInputs.failIfInvalid("enquiryheading", sub);

        String loc = req.queryParams("location");
        InvalidInputs.failIfInvalid("location", loc);

        String instr = req.queryParams("anyadditionalinstruction");
        InvalidInputs.failIfInvalid("anyadditionalinstruction", instr);

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
