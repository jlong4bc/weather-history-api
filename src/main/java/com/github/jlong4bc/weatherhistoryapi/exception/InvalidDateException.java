package com.github.jlong4bc.weatherhistoryapi.exception;

/**
 * Thrown when the date format or bounds of a date is invalid.
 */
public class InvalidDateException extends RuntimeException
{
    public InvalidDateException(String message) {
        super(message);
    }
}
