package com.github.jlong4bc.weatherhistoryapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Provides global exception handling using chain-of-responsibility.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler
{
    private static final String MESSAGE_KEY = "message";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({
            CountryNotFoundException.class,
            StateProvinceNotFoundException.class,
            TokenNotFoundException.class,
            NoaaStationNotFoundException.class,
            NoaaWeatherNotFoundException.class
    })
    public Map<String,String> handleNotFoundExceptions(RuntimeException ex)
    {
        String msg = ex.getMessage();
        String defaultMsg =
                switch(ex) {
                    case CountryNotFoundException _ -> "The country specified is not available.";
                    case StateProvinceNotFoundException _ -> "The state / province specified is not available";
                    case TokenNotFoundException _ -> "The token was not found.  Please use bearer token.";
                    case NoaaStationNotFoundException _ -> "A NOAA weather station cannot be found.";
                    case GeoLocationNotFoundException _ -> "Coordinates of the specific location could not be found.";
                    default -> ex.getMessage();
                };

        msg = StringUtils.defaultIfEmpty(msg, defaultMsg);
        return Map.of(MESSAGE_KEY, msg);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            DateRangeException.class,
            InvalidCityException.class,
            InvalidDateException.class,
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
        return Map.of(MESSAGE_KEY, msg);
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({
            InternalServerException.class,
            ExternalApiException.class
    })
    public Map<String,String> handleInternalServerException(RuntimeException ex)
    {
        log.error("Exception sent to the user. {}", ex.getMessage());

        String msg = ex.getMessage();
        if (ex instanceof ExternalApiException) {
            if (msg.contains("503 Service Unavailable")) {
                msg = "Services are currently unavailable.  Try again later.";
            }
        }

        msg = StringUtils.defaultIfEmpty(msg, "An internal error has occurred. Try again later.");
        return Map.of(MESSAGE_KEY, msg);
    }
}
