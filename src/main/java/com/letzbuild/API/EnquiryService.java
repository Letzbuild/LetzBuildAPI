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

        String fname = req.queryParams("fname");
        InvalidInputs.failIfInvalid("fname", fname);

        String lname = req.queryParams("lname");
        InvalidInputs.failIfInvalid("lname", lname);

        String org = req.queryParams("org");
        InvalidInputs.failIfInvalid("org", org);

        String mobile = req.queryParams("mobile");
        InvalidInputs.failIfInvalid("mobile", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String qty = req.queryParams("qty");
        InvalidInputs.failIfInvalid("qty", qty);

        String orderSpec = req.queryParams("orderSpec");
        InvalidInputs.failIfInvalid("orderSpec", qty);

        String sub = req.queryParams("sub");
        InvalidInputs.failIfInvalid("sub", sub);

        String needDate = req.queryParams("needDate");
        InvalidInputs.failIfInvalid("needDate", needDate);

        String budget = req.queryParams("budget");
        InvalidInputs.failIfInvalid("budget", budget);

        String loc = req.queryParams("loc");
        InvalidInputs.failIfInvalid("loc", loc);

        String freq = req.queryParams("freq");
        InvalidInputs.failIfInvalid("freq", freq);

        String reason = req.queryParams("reason");
        InvalidInputs.failIfInvalid("reason", reason);

        String instr = req.queryParams("instr");
        InvalidInputs.failIfInvalid("instr", instr);

        DBObject doc = new BasicDBObject();
        doc.put("pcode", pcode);
        doc.put("fname", fname);
        doc.put("lname", lname);
        doc.put("org", org);
        doc.put("mobile", mobile);
        doc.put("email", email);
        doc.put("qty", qty);
        doc.put("orderSpec", orderSpec);
        doc.put("sub", sub);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        doc.put("needDate", formatter.parse(needDate));
        doc.put("budget", budget);
        doc.put("loc", loc);
        doc.put("freq", freq);
        doc.put("reason", reason);
        doc.put("instr", instr);
        doc.put("enqDate", new Date());

        productEnquiriesCollection_.insert(doc);
    }

    public void sendBOMEnquiry(Request req) throws ParseException {

        String fname = req.queryParams("fname");
        InvalidInputs.failIfInvalid("fname", fname);

        String lname = req.queryParams("lname");
        InvalidInputs.failIfInvalid("lname", lname);

        String org = req.queryParams("org");
        InvalidInputs.failIfInvalid("org", org);

        String mobile = req.queryParams("mobile");
        InvalidInputs.failIfInvalid("mobile", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String sub = req.queryParams("sub");
        InvalidInputs.failIfInvalid("sub", sub);

        String loc = req.queryParams("loc");
        InvalidInputs.failIfInvalid("loc", loc);

        String instr = req.queryParams("instr");
        InvalidInputs.failIfInvalid("instr", instr);

        DBObject doc = new BasicDBObject();
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

    public void sendPMSEnquiry(Request req) throws ParseException {

        String fname = req.queryParams("fname");
        InvalidInputs.failIfInvalid("fname", fname);

        String lname = req.queryParams("lname");
        InvalidInputs.failIfInvalid("lname", lname);

        String org = req.queryParams("org");
        InvalidInputs.failIfInvalid("org", org);

        String mobile = req.queryParams("mobile");
        InvalidInputs.failIfInvalid("mobile", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String sub = req.queryParams("sub");
        InvalidInputs.failIfInvalid("sub", sub);

        String loc = req.queryParams("loc");
        InvalidInputs.failIfInvalid("loc", loc);

        String instr = req.queryParams("instr");
        InvalidInputs.failIfInvalid("instr", instr);

        DBObject doc = new BasicDBObject();
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

    public void sendQSEnquiry(Request req) throws ParseException {

        String fname = req.queryParams("fname");
        InvalidInputs.failIfInvalid("fname", fname);

        String lname = req.queryParams("lname");
        InvalidInputs.failIfInvalid("lname", lname);

        String org = req.queryParams("org");
        InvalidInputs.failIfInvalid("org", org);

        String mobile = req.queryParams("mobile");
        InvalidInputs.failIfInvalid("mobile", mobile);

        String email = req.queryParams("email");
        InvalidInputs.failIfInvalid("email", email);

        String sub = req.queryParams("sub");
        InvalidInputs.failIfInvalid("sub", sub);

        String loc = req.queryParams("loc");
        InvalidInputs.failIfInvalid("loc", loc);

        String instr = req.queryParams("instr");
        InvalidInputs.failIfInvalid("instr", instr);

        DBObject doc = new BasicDBObject();
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
}
