package com.compass.demo_park_api.errors;

public class NotFoundError extends RuntimeException {
    public NotFoundError() {
        super("Resource not found");
    }
}
