package com.github.jlong4bc.weatherhistoryapi;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
     * Defines the main endpoint for weather history API
     * @param country allowed as input but initially only works for USA
     * @param stateProvince identifies as the state / province within the country for weather history
     * @param city identifies the city of requested weather history
     * @param fromDateParam identifies the starting date of the weather history
     * @param toDateParam identifies the ending date of the weather history
     * @param request the HttpServletRequest from the Servlet context to retrieve the bearer token.
     * @return a JavaScript object identifying requested weather history of the area and time specified
     */
    @GetMapping("/weather/{country}/{state-province}/{city}")
    public ResponseEntity<List<WeatherHistory>> retrieveWeatherHistory(@PathVariable String country,
                                                                 @PathVariable("state-province") String stateProvince,
                                                                 @PathVariable String city,
                                                                 @RequestParam(required = false) String fromDateParam,
                                                                 @RequestParam(required = false) String toDateParam,
                                                                 HttpServletRequest request)
    {
        // There may be a more elegant way to retrieve the bearer token, but this is A way.
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Strip out the scheme because only the token is needed, but watch for missing tokens.
        token = token != null ? token.replace("Bearer ", "") : "";

        log.info("Request: ... country: {}, state-province: {}, city: {}, fromDate: {}, toDate: {}, token: {}",
                country, stateProvince, city, fromDateParam, toDateParam, token.substring(0,3)+"...");

        RawInputData rawInput = new RawInputData(country, stateProvince, city, fromDateParam, toDateParam, token);
        validateInput(rawInput);
        InputData inputData = convertRawInputDataToInputData(rawInput);
        List<WeatherHistory> specifiedHistory = service.retrieveWeatherHistory(inputData);
        return new ResponseEntity<>(specifiedHistory, HttpStatus.OK);
    }

    // Validates the attributes of the raw input
    private void validateInput(RawInputData rawInput)
    {
        // Check for existing token first because it's easy to forget to include in a request.
        InputValidation.validateTokenExists(rawInput.noaaToken());
        InputValidation.validateCity(rawInput.cityName());
        InputValidation.validateCountry(rawInput.countryName());
        InputValidation.validateStateProvince(rawInput.stateProvinceName());

        // Create local variable because of reuse which reduces verbosity.
        String fromDateStr = rawInput.fromDateStr();
        String toDateStr = rawInput.toDateStr();
        if (fromDateStr != null) {
            InputValidation.validateIso8601Date(fromDateStr);
        }
        if (toDateStr != null) {
            InputValidation.validateIso8601Date(toDateStr);
        }

        // Change to actual date objects to validate date range rules.
        LocalDate fromDate = StringUtils.isEmpty(fromDateStr) ? null : LocalDate.parse(fromDateStr, DateTimeFormatter.ISO_DATE);
        LocalDate toDate = StringUtils.isEmpty(toDateStr) ? null : LocalDate.parse(toDateStr, DateTimeFormatter.ISO_DATE);

        InputValidation.validateDateRange(fromDate, toDate);
    }

    // Processes the raw input by converting it into attributes the weather service can use.
    private InputData convertRawInputDataToInputData(RawInputData rawInput)
    {
        Country country = Country.of(rawInput.countryName());
        StateProvince stateProvince = StateProvince.of(rawInput.stateProvinceName());
        String city = rawInput.cityName();

        // Having to repeat code from the validation method here because the input transformation was kept separate.
        String fromDateStr = rawInput.fromDateStr();
        String toDateStr = rawInput.toDateStr();
        LocalDate fromDate = StringUtils.isEmpty(fromDateStr) ? null : LocalDate.parse(fromDateStr, DateTimeFormatter.ISO_DATE);
        LocalDate toDate = StringUtils.isEmpty(toDateStr) ? null : LocalDate.parse(toDateStr, DateTimeFormatter.ISO_DATE);

        // When both from & to dates are null, make them yesterday's date.
        if (fromDate == null && toDate == null) {
            fromDate = LocalDate.now().minusDays(1);
            toDate = LocalDate.now().minusDays(1);
        }

        // Using a separate variable to improve readability - makes the return easier to read.
        String token = rawInput.noaaToken();

        return new InputData(country, stateProvince, city, fromDate, toDate, token);
    }
}
