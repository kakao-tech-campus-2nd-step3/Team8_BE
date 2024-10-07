package com.example.sinitto.callback.exception;

import jakarta.persistence.EntityNotFoundException;

public class GuardMismatchException extends EntityNotFoundException {

    public GuardMismatchException(String message) {
        super(message);
    }
}
