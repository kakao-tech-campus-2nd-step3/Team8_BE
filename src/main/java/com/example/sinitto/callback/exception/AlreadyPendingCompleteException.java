package com.example.sinitto.callback.exception;

public class AlreadyPendingCompleteException extends ConflictException {

    public AlreadyPendingCompleteException(String message) {
        super(message);
    }
}
