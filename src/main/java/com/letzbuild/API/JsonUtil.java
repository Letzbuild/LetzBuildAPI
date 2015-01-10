package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import spark.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class JsonUtil {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }

    public static BasicDBObject constructPageObject(long total, int currPage, int limit) {
        BasicDBObject out = new BasicDBObject();
        out.put("total", total);
        out.put("currPage", ++currPage);
        out.put("limit", limit);
        return out;
    }

    public static Map<String, Object> constructPaginatedOut(Properties p, Request req,
                  BasicDBObject query, DBCollection dbCol, BasicDBObject fields) {

        Map<String, Object> out = null;

        int limit = Integer.parseInt(p.getProperty("pageLimit"));
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

        out = new HashMap<String, Object>();

        long count = dbCol.count(query);
        DBCursor cursor = null;
        if (fields != null)
            cursor = dbCol.find(query, fields).skip(page * limit).limit(limit);
        else
            cursor = dbCol.find(query).skip(page * limit).limit(limit);

        try {
            out.put("pagination", JsonUtil.constructPageObject(count, page, limit));
            out.put("result", cursor.toArray());
        } finally {
            cursor.close();
        }

        return out;
    }

}
