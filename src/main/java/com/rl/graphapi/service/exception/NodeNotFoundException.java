package com.rl.graphapi.service.exception;

public class NodeNotFoundException extends GraphRuntimeException {

    public NodeNotFoundException(final String message) {
        super(message);
    }
}
