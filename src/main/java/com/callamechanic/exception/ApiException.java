package com.callamechanic.exception;

public class ApiException extends RuntimeException {

    private final String code;
    private final Object details;

    public ApiException(String code, String message, Object details) {
        super(message);
        this.code    = code;
        this.details = details;
    }

    public String getCode()    { return code;    }
    public Object getDetails() { return details; }
}