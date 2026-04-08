package com.github.jlong4bc.weatherhistoryapi;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * This class provides endpoint definition and traffic management for Weather History API requests.
 */

@Slf4j
@AllArgsConstructor
@RestController
public class WeatherController
{
    private final WeatherService service;

    /**
     * Defines the main endpoint for weather history
     * @param country allowed as input but initially only works for USA
     * @param stateProvince identifies as the state / province within the country for weather history
     * @param city identifies the city of requested weather history
     * @param fromDate identifies the starting date of the weather history
     * @param toDate identifies the ending date of the weather history
     * @param request the HttpServletRequest from the Servlet context to retrieve the bearer token.
     * @return a JavaScript object identifying requested weather history of the area and time specified
     */
    @GetMapping("/weather/{country}/{state-province}/{city}")
    public ResponseEntity<WeatherHistory> retrieveWeatherHistory(@PathVariable String country,
                                                    @PathVariable("state-province") String stateProvince,
                                                    @PathVariable String city,
                                                    @RequestParam(required = false) String fromDate,
                                                    @RequestParam(required = false) String toDate,
                                                                 HttpServletRequest request)
    {
        Optional<String> fromDateOpt = Optional.ofNullable(fromDate);
        Optional<String> toDateOpt = Optional.ofNullable(toDate);

        // There may be a more elegant way to retrieve the bearer token, but this is A way.
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Strip out the scheme because only the token is needed.
        token = token.replace("Bearer ", "");

        log.info("country: {}, state-province: {}, city: {}, fromDate: {}, toDate: {}, token: {}",
                country, stateProvince, city, fromDateOpt.orElse("-"), toDateOpt.orElse("-"), token);

        WeatherHistory wHistory = service.retrieveWeatherHistory();

        return new ResponseEntity<>(wHistory, HttpStatus.OK);
    }
}
