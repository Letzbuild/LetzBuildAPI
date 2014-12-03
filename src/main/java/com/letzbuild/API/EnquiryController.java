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
        post("/enquiries/supplier/add", (req, res) -> {
            try {
                enquiryService.sendSupplierEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "failure";
            }
            res.status(201); // 201 Created
            return "success";
        }, json());

        // retrieve product based enquiries
        get("/enquiries/supplier/retrieve", (req, res) -> {
            List<DBObject> list = enquiryService.retrieveSupplierEnquiries(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No enquiry results found");
        }, json());

        /*********************************************************************************************/

        // send enquiry for this product to LetzBuild
        post("/enquiries/product/add", (req, res) -> {
            try {
                enquiryService.sendProductEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "failure";
            }
            res.status(201); // 201 Created
            return "success";
        }, json());

        // retrieve product based enquiries
        get("/enquiries/product/retrieve", (req, res) -> {
            List<DBObject> list = enquiryService.retrieveProductEnquiries(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No enquiry results found");
        }, json());

        /*********************************************************************************************/

        // send a BOM enquiry
        post("/enquiries/bom/add", (req, res) -> {
            try {
                enquiryService.sendBOMEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "failure";
            }
            res.status(201); // 201 Created
            return "success";
        }, json());

        // retrieve product based enquiries
        get("/enquiries/bom/retrieve", (req, res) -> {
            List<DBObject> list = enquiryService.retrieveBOMEnquiries(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No enquiry results found");
        }, json());

        /*********************************************************************************************/

        // send an PMS enquiry
        post("/enquiries/pms/add", (req, res) -> {
            try {
                enquiryService.sendPMSEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "failure";
            }
            res.status(201); // 201 Created
            return "success";
        }, json());

        // retrieve product based enquiries
        get("/enquiries/pms/retrieve", (req, res) -> {
            List<DBObject> list = enquiryService.retrievePMSEnquiries(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No enquiry results found");
        }, json());

        /*********************************************************************************************/

        // send an Quotation Services enquiry
        post("/enquiries/qs/add", (req, res) -> {
            try {
                enquiryService.sendQSEnquiry(req);
            } catch (ParseException e) {
                res.status(500);
                return "failure";
            }
            res.status(201); // 201 Created
            return "success";
        }, json());

        // retrieve product based enquiries
        get("/enquiries/qs/retrieve", (req, res) -> {
            List<DBObject> list = enquiryService.retrieveQSEnquiries(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No enquiry results found");
        }, json());

    }
}