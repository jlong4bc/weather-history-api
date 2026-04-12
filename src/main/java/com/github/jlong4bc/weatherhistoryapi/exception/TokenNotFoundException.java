package com.github.jlong4bc.weatherhistoryapi.exception;

/**
 * This exception provides specific context and is thrown when a token is not found in the header.
 */
public class TokenNotFoundException extends RuntimeException
{
    public TokenNotFoundException() {
        super();
    }
}
