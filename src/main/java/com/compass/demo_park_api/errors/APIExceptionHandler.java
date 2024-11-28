package com.compass.demo_park_api.errors;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class APIExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request,
            BindingResult bindingResult
    ) {
        ErrorMessage errorMessage = new ErrorMessage(
                request,
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Invalid fields",
                bindingResult
        );
        log.error("API ERROR: ", ex);
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }

    @ExceptionHandler(UserNameUniqueError.class)
    public ResponseEntity<ErrorMessage> userNameUniqueException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        ErrorMessage errorMessage = new ErrorMessage(
                request,
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        log.error("API ERROR: ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }

    @ExceptionHandler(WrongPasswordError.class)
    public ResponseEntity<ErrorMessage> wrongPasswordException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        ErrorMessage errorMessage = new ErrorMessage(
                request,
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        log.error("API ERROR: ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }

    @ExceptionHandler(ConfirmPasswordError.class)
    public ResponseEntity<ErrorMessage> confirmPasswordException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        ErrorMessage errorMessage = new ErrorMessage(
                request,
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
        log.error("API ERROR: ", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }

    @ExceptionHandler(NotFoundError.class)
    public ResponseEntity<ErrorMessage> notFoundException(
            RuntimeException ex,
            HttpServletRequest request
    ) {
        ErrorMessage errorMessage = new ErrorMessage(
                request,
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        log.error("API ERROR: ", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(errorMessage);
    }
}