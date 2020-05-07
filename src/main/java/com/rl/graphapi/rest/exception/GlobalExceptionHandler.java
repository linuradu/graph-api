package com.rl.graphapi.rest.exception;

import com.rl.graphapi.service.exception.GraphRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(GraphRuntimeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ResponseBody
    public HttpError handleException(final GraphRuntimeException exception) {
        LOG.error("Exception handled:", exception);

        return new HttpError(exception.getMessage());
    }
}
