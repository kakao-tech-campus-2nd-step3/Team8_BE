package com.example.sinitto.callback.exception;

public class AlreadyWaitingException extends ForbiddenException {

    public AlreadyWaitingException(String message) {
        super(message);
    }
}
