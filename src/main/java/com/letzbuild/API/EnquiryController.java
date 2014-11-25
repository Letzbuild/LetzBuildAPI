package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.DBObject;

import java.text.ParseException;
import java.util.List;

import static com.letzbuild.API.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;


public class EnquiryController {

    public EnquiryController(final EnquiryService enquiryService) {

        // send enquiry for this product to LetzBuild
        post("/enquiries/product/add", (req, res) -> {
            try {
                enquiryService.sendProductEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "Failed to create enquiry";
            }
            res.status(201); // 201 Created
            return "Successfully created enquiry";
        }, json());

        // send a BOM enquiry
        post("/enquiries/bom/add", (req, res) -> {
            try {
                enquiryService.sendBOMEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "Failed to create enquiry";
            }
            res.status(201); // 201 Created
            return "Successfully created enquiry";
        }, json());

        // send an PMS enquiry
        post("/enquiries/pms/add", (req, res) -> {
            try {
                enquiryService.sendPMSEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "Failed to create enquiry";
            }
            res.status(201); // 201 Created
            return "Successfully created enquiry";
        }, json());

        // send an Quotation Services enquiry
        post("/enquiries/qs/add", (req, res) -> {
            try {
                enquiryService.sendQSEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "Failed to create enquiry";
            }
            res.status(201); // 201 Created
            return "Successfully created enquiry";
        }, json());
    }
}