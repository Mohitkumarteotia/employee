package com.service.employee.exception.custom;

public class RateLimitExceededException
        extends RuntimeException {

    public RateLimitExceededException(String message) {
        super(message);
    }
}