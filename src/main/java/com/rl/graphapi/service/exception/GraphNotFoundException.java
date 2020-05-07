package com.rl.graphapi.service.exception;

public class GraphNotFoundException extends GraphRuntimeException {

    public GraphNotFoundException(final String message) {
        super(message);
    }
}
