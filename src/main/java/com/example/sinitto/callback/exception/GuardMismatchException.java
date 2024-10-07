package com.example.sinitto.callback.exception;

public class GuardMismatchException extends NotFoundException {

    public GuardMismatchException(String message) {
        super(message);
    }
}
