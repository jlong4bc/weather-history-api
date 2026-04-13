package com.github.jlong4bc.weatherhistoryapi.exception;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Provides global exception handling using chain-of-responsibility.
 */
@RestControllerAdvice
public class GlobalExceptionHandler
{
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            CountryNotFoundException.class,
            StateProvinceNotFoundException.class,
            TokenNotFoundException.class
    })
    public Map<String,String> handleNotFoundExceptions(RuntimeException ex)
    {
        String msg = ex.getMessage();
        String defaultMsg =
                switch(ex) {
                    case CountryNotFoundException _ -> "The country specified is not available.";
                    case StateProvinceNotFoundException _ -> "The state / province specified is not available";
                    case TokenNotFoundException _ -> "The token was not found.  Please use bearer token.";
                    default -> ex.getMessage();
                };

        msg = StringUtils.defaultIfEmpty(msg, defaultMsg);
        return Map.of("message", msg);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            DateRangeException.class,
            InvalidCityException.class,
            InvalidDateException.class
    })
    public Map<String,String> handleBadRequestExceptions(RuntimeException ex)
    {
        String msg = ex.getMessage();
        String defaultMsg =
                switch(ex) {
                    case DateRangeException _ -> "A problem with the date range was found.";
                    case InvalidCityException _ -> "The specified city is not valid.";
                    case InvalidDateException _ -> "The date was incorrect.  Please use ISO 8601 format.";
                    default -> ex.getMessage();
                };
        msg = StringUtils.defaultIfEmpty(msg, defaultMsg);
        return Map.of("message", msg);
    }
}
