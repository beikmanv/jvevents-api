package com.northcoders.jvevents.exception;

public class UnauthenticatedUserException extends RuntimeException {
    public UnauthenticatedUserException(String  message) {
        super(message);
    }
}
