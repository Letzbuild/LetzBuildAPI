package com.letzbuild.API;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by venky on 22/09/14.
 */
public class Main {
    public static void main(String[] args) throws IOException {

        Main m = new Main();
        Properties p = m.readConfigFile();

        String mongoURIString = p.getProperty("connStr");

        final MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURIString));
        final DB letzbuildDB = mongoClient.getDB("letzbuild");

        new UserController(new UserService(letzbuildDB));
        new ProductController(new ProductService(letzbuildDB, p));

        UserService userService = new UserService(letzbuildDB);
        new BuyerController(new BuyerService(letzbuildDB), userService);
    }

    public Properties readConfigFile() {
        Properties prop = null;
        InputStream input = null;

        try {

            String filename = "config.properties";
            input = getClass().getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                System.out.println("Sorry, unable to find " + filename);
                return prop;
            }

            prop = new Properties();
            prop.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return prop;
    }
}
