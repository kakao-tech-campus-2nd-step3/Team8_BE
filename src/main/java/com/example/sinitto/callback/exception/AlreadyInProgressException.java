package com.example.sinitto.callback.exception;

public class AlreadyInProgressException extends ConflictException {

    public AlreadyInProgressException(String message) {
        super(message);
    }
}
