package com.github.jlong4bc.weatherhistoryapi.exception;

/**
 * Identifies an issue with the date range specified
 */
public class DateRangeException extends RuntimeException
{
    public DateRangeException(String message) {
        super(message);
    }
}
