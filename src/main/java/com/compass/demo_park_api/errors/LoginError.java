package com.compass.demo_park_api.errors;

public class LoginError extends RuntimeException {
    public LoginError() {
        super("Username or password is incorrect.");
    }
}
