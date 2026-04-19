package com.github.jlong4bc.weatherhistoryapi.exception;

/**
 * Represents a general internal server exception equivalent to an HTTP 500 status.
 * It's a way to specifically communicate a message to the user about errors on the server side.
 */
public class InternalServerException extends RuntimeException
{
    public InternalServerException(String message) {
        super(message);
    }
}
