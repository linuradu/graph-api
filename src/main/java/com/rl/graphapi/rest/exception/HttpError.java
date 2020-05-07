package com.rl.graphapi.rest.exception;

public class HttpError {

    private String message;

    public HttpError(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
