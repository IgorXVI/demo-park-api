package com.compass.demo_park_api.errors;

public class UserNameUniqueError extends RuntimeException {
    public UserNameUniqueError() {
        super("User name already exists");
    }
}
