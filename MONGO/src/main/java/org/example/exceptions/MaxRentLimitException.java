package org.example.exceptions;

public class MaxRentLimitException extends RuntimeException {
    public MaxRentLimitException(String message) {
        super(message);
    }
}
