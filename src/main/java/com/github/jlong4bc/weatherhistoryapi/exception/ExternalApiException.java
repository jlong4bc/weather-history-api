package com.github.jlong4bc.weatherhistoryapi.exception;

/**
 * An exception that indicates an issue with the external API.
 */
public class ExternalApiException extends RuntimeException
{
    public ExternalApiException(String message) {
        super(message);
    }
}
