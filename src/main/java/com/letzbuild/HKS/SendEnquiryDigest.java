package com.letzbuild.HKS;

import com.mongodb.*;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import org.codemonkey.simplejavamail.Email;
import org.codemonkey.simplejavamail.Mailer;
import org.codemonkey.simplejavamail.TransportStrategy;

import javax.mail.Message;
import javax.mail.Session;
import java.io.*;
import java.net.PasswordAuthentication;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by venky on 22/08/14.
 */
public class SendEnquiryDigest {
    public static void main(String[] args) throws Exception {

        SendEnquiryDigest m = new SendEnquiryDigest();
        Properties p = ConfigurationUtils.readConfigFile();

        String mongoURIString = p.getProperty("connStr");
        MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURIString));

        DB letzbuildDB = mongoClient.getDB("letzbuild");

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        SimpleHash root = new SimpleHash();

        root.put("date", dateFormat.format(date));
        root.put("prodEnqCount", m.getProductEnquiryCount(letzbuildDB));
        root.put("suppEnqCount", m.getSupplierEnquiryCount(letzbuildDB));

        /*String emailBody = ConfigurationUtils.processFreemarkerTemplate("EnquiryDigest.ftl", root);

        Map<String, String> recipients = new HashMap<String, String>();
        recipients.put("Rama", "rama@letzbuild.com");
        recipients.put("Ganesh", "rama@letzbuild.com");
        recipients.put("Nathan", "rama@letzbuild.com");
        recipients.put("Ops", "operations@letzbuild.com");

        ConfigurationUtils.sendEmail("Guest User Enquiry Digest", emailBody, recipients);

        System.out.println("Email Sent");*/

        mongoClient.close();
    }

    private long getProductEnquiryCount(DB letzbuildDB) {
        long ret = 0;

        DBCollection prodEnqCol = letzbuildDB.getCollection("product_enquiries");
        Date d = new Date();

        DBObject query = QueryBuilder.start().put("enqDate").greaterThanEquals(d).get();

        ret = prodEnqCol.count(query);

        System.out.println("prodEnq-" + ret);

        return ret;
    }

    private long getSupplierEnquiryCount(DB letzbuildDB) {
        long ret = 0;

        DBCollection suppEnqCol = letzbuildDB.getCollection("supplier_enquiries");

        ret = suppEnqCol.count(new BasicDBObject("enqDate", new Date()));

        System.out.println("suppEnq-" + ret);

        return ret;
    }
}
