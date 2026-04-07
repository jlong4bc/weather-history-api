package com.github.jlong4bc.weatherhistoryapi;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * This defines the root path as the documentation URL
     * @return the location of the documentation
     */
    @GetMapping("/")
    public String getDocumentation()
    {
        return "/swagger-ui/index.html";
    }

    /**
     * Defines the main endpoint for weather history
     * @param country allowed as input but initially only works for USA
     * @param stateProvince identifies as the state / province within the country for weather history
     * @param city identifies the city of requested weather history
     * @param fromDate identifies the starting date of the weather history
     * @param toDate identifies the ending date of the weather history
     * @return a JavaScript object identifying requested weather history of the area and time specified
     */
    @GetMapping("/weather/{country}/{state-province}/{city}")
    public ResponseEntity<WeatherHistory> retrieveWeatherHistory(@PathVariable String country,
                                                    @PathVariable("state-province") String stateProvince,
                                                    @PathVariable String city,
                                                    @RequestParam Optional<String> fromDate,
                                                    @RequestParam Optional<String> toDate,
                                                    @RequestHeader("Authorization") String token)
    {
        // Strip out the scheme because only the token is needed.
        token = token.replace("Bearer ", "");

        log.info("country: {}, state-province: {}, city: {}, fromDate: {}, toDate: {}, token: {}",
                country, stateProvince, city, fromDate, toDate, token);

        WeatherHistory wHistory = service.retrieveWeatherHistory();

        return new ResponseEntity<>(wHistory, HttpStatus.OK);
    }
}
