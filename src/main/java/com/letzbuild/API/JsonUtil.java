package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import spark.*;

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
}
