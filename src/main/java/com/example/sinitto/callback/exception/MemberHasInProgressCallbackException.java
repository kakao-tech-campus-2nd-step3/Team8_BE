package com.example.sinitto.callback.exception;

public class MemberHasInProgressCallbackException extends ConflictException {

    public MemberHasInProgressCallbackException(String message) {
        super(message);
    }
}
