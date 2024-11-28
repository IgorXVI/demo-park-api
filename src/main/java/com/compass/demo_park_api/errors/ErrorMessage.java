package com.compass.demo_park_api.errors;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
@ToString
public class ErrorMessage {
    private final String message;

    private final String method;

    private final String path;

    private final int status;

    private final String statusText;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message) {
        this.message = message;
        this.method = request.getMethod();
        this.path = request.getRequestURI();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
    }

    public ErrorMessage(HttpServletRequest request, HttpStatus status, String message, BindingResult bindingResult) {
        this.message = message;
        this.method = request.getMethod();
        this.path = request.getRequestURI();
        this.status = status.value();
        this.statusText = status.getReasonPhrase();
        addErrors(bindingResult);
    }

    private void addErrors(BindingResult bindingResult) {
        this.errors = new HashMap<>();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            this.errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}