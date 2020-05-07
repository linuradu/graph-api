package com.rl.graphapi.service.exception;

public abstract class GraphRuntimeException extends RuntimeException {

    GraphRuntimeException(final String message) {
        super(message);
    }
}
