package com.northcoders.jvevents.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<?> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        if (e instanceof EventNotFoundException) {
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", e.getMessage());
        } else if (e instanceof AppUserNotFoundException) {
            response.put("status", 404);
            response.put("error", "Not Found");
            response.put("message", e.getMessage());
        } else if (e instanceof MissingFieldException) {
            response.put("status", 400);
            response.put("error", "Bad Request");
            response.put("message", e.getMessage());
        } else if (e instanceof UnauthenticatedUserException) {
            response.put("status", 401);
            response.put("error", "Unauthorized");
            response.put("message", e.getMessage());
        } else {
            response.put("status", 500);
            response.put("error", "Internal Server Error");
            response.put("message", "Unexpected error: " + e.getMessage());
        }
        response.put("timestamp", Instant.now().toString());

        // Determine status based on exception type
        HttpStatus status;
        if (e instanceof EventNotFoundException || e instanceof AppUserNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (e instanceof MissingFieldException) {
            status = HttpStatus.BAD_REQUEST;
        } else if (e instanceof UnauthenticatedUserException) {
            status = HttpStatus.UNAUTHORIZED;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(response, status);
    }

    // Specific handlers for other exceptions can be added if needed, for example:
    @ExceptionHandler(UnauthenticatedUserException.class)
    public ResponseEntity<Object> handleUnauthenticatedUser(UnauthenticatedUserException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 401);
        response.put("error", "Unauthorized");
        response.put("message", ex.getMessage());
        response.put("timestamp", Instant.now().toString());

        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseBody
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", 404);
        response.put("error", "Not Found");
        response.put("message", e.getMessage());
        response.put("timestamp", Instant.now().toString());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}

