package com.compass.demo_park_api.errors;

public class WrongPasswordError extends RuntimeException {
    public WrongPasswordError() {
        super("Wrong password");
    }
}
