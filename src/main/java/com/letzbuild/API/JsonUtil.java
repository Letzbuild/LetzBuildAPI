package com.letzbuild.API;

/**
 * Created by venky on 22/09/14.
 */
import com.google.gson.Gson;
import spark.*;

public class JsonUtil {

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonUtil::toJson;
    }
}
