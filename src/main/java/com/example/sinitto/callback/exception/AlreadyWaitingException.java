package com.example.sinitto.callback.exception;

public class AlreadyWaitingException extends ConflictException {

    public AlreadyWaitingException(String message) {
        super(message);
    }
}
