package com.kadiraksoy.UnitTestExample.exception;

public class InvalidTodoRequest extends RuntimeException {
    public InvalidTodoRequest() {
        super();
    }

    public InvalidTodoRequest(String message) {
        super(message);
    }
}