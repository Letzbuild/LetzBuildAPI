package com.letzbuild.API;

/**
 * Created by venky on 24/10/14.
 */
public class InvalidInputs {
    public static void failIfInvalid(String name, String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Parameter " + name + " cannot be empty");
        }
    }
}
