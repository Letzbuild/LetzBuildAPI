package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.mongodb.DBObject;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import static com.letzbuild.API.JsonUtil.json;
import static spark.Spark.get;
import static spark.Spark.post;


public class EnquiryController {

    public EnquiryController(final EnquiryService enquiryService, final ProductService productService,
                             final SupplierService supplierService) {

        // send enquiry for this product to LetzBuild
        post("/enquiries/supplier/add", (req, res) -> {
            try {
                String pcode = req.queryParams("pcode");
                InvalidInputs.failIfInvalid("pcode", pcode);
                DBObject prodObj = productService.retrieveProduct(pcode);

                String scode = req.queryParams("scode");
                InvalidInputs.failIfInvalid("scode", scode);
                DBObject suppObj = supplierService.retrieveSupplier(scode);

                enquiryService.sendSupplierEnquiry(req, prodObj, suppObj);
            } catch (ParseException e) {
                res.status(500);
                return "failure";
            }
            res.status(201); // 201 Created
            return "success";
        }, json());

        // retrieve product based enquiries
        get("/enquiries/supplier/retrieve", (req, res) -> {
            Map<String, Object> list = enquiryService.retrieveSupplierEnquiries(req);
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
                String pcode = req.queryParams("pcode");
                InvalidInputs.failIfInvalid("pcode", pcode);
                DBObject prodObj = productService.retrieveProduct(pcode);

                enquiryService.sendProductEnquiry(req, prodObj);
            } catch (ParseException e) {
                res.status(500);
                return "failure";
            }
            res.status(201); // 201 Created
            return "success";
        }, json());

        // retrieve product based enquiries
        get("/enquiries/product/retrieve", (req, res) -> {
            Map<String, Object> list = enquiryService.retrieveProductEnquiries(req);
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
            Map<String, Object> list = enquiryService.retrieveBOMEnquiries(req);
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
            Map<String, Object> list = enquiryService.retrievePMSEnquiries(req);
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
            Map<String, Object> list = enquiryService.retrieveQSEnquiries(req);
            if (list != null) {
                return list;
            }
            res.status(400);
            return new ResponseError("No enquiry results found");
        }, json());

    }
}