package com.kadiraksoy.UnitTestExample.exception;

public class InvalidUserRequest extends RuntimeException{
    public InvalidUserRequest() {
        super();
    }

    public InvalidUserRequest(String message) {
        super(message);
    }
}