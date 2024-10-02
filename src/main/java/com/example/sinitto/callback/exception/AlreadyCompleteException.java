package com.example.sinitto.callback.exception;

public class AlreadyCompleteException extends ConflictException {

    public AlreadyCompleteException(String message) {
        super(message);
    }
}
