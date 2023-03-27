package com.example.smessenger.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.GONE;

@ResponseStatus(value = GONE)
public class GoneException extends RuntimeException {
    public GoneException() {
        super("Gone exception occurred");
    }

    public GoneException(String message) {
        super(message);
    }

    public GoneException(String message, Throwable cause) {
        super(message, cause);
    }

    public GoneException(Throwable cause) {
        super(cause);
    }

    protected GoneException(String message, Throwable cause,
                            boolean enableSuppression,
                            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
