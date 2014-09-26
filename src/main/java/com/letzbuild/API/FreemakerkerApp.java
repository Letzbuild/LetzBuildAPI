package com.pearson.API;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by venky on 08/08/14.
 */
public class FreemakerkerApp {
    public static void main(String[] args) {
        Configuration cfg = new Configuration();

        cfg.setClassForTemplateLoading(
                FreemakerkerApp.class, "/");

        try {
            Template helloTemplate = cfg.getTemplate("hello.ftl");
            StringWriter writer = new StringWriter();
            Map<String, Object> helloMap = new HashMap<String, Object>();
            helloMap.put("name", "Venky");

            helloTemplate.process(helloMap, writer);

            System.out.println(writer);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
