package com.letzbuild.API;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by venky on 08/08/14.
 */
public class SparkRoutes {
    public static void main(String[] args) {
        /*Spark.get(new Route("/") {
            @Override
            public Object handle(final Request request, final Response response) {
                return "Hello World from spark\n";
            }
        });

        Spark.get(new Route("/test") {
            @Override
            public Object handle(final Request request, final Response response) {
                return "This is a test page\n";
            }
        });

        Spark.get(new Route("/echo/:thing") {
            @Override
            public Object handle(final Request request, final Response response) {
                return request.params(":thing");
            }
        });*/
    }
}
