package com.compass.demo_park_api.errors;

public class ConfirmPasswordError extends RuntimeException {
    public ConfirmPasswordError() {
        super("New passwords do not match");
    }
}
