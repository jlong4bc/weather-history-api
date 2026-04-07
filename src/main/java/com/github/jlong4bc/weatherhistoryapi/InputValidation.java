package com.github.jlong4bc.weatherhistoryapi;

import com.github.jlong4bc.weatherhistoryapi.exception.CountryNotFoundException;
import com.github.jlong4bc.weatherhistoryapi.exception.InvalidCityException;
import com.github.jlong4bc.weatherhistoryapi.exception.InvalidDateException;
import com.github.jlong4bc.weatherhistoryapi.exception.StateProvinceNotFoundException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * A class that validates raw input from the user to ensure it's not harmful for processing
 * and to inform the user of input error.
 */
public class InputValidation
{
    // Focus on allowed characters like a space, apostrophe and "A" - "z";
    // Allow no more than 25 characters in a city name which allows for most city names
    private final static Pattern VALID_CITY_PATTERN = Pattern.compile("(\\s|'|-|[A-Z]|[a-z]){0,25}");



    /**
     * Provides country name validity
     * @param countryName the country name to validate
     */
    public void validateCountry(String countryName)
    {
        // Only allow countries that can be processed.
        if (Country.of(countryName) == null) {
            throw new CountryNotFoundException();
        }
    }

    /**
     * Provides state or province name validity
     * @param stateProvinceName the state or province to validate
     */
    public void validateStateProvince(String stateProvinceName)
    {
        if (StateProvince.of(stateProvinceName) == null) {
            throw new StateProvinceNotFoundException();
        }
    }

    /**
     * Provides basic validation of alphabetic characters and typical characters in proper names
     * @param cityName the city to validate
     */
    public void validateCity(String cityName)
    {
        if (!VALID_CITY_PATTERN.matcher(cityName).matches()) {
            throw new InvalidCityException();
        }
    }

    /**
     * Provides validation of ISO 8601 date string with limited year range.
     * @param dateString an ISO 8601 formatted date string limit to a 100-year range
     *                   beginning from the current year and counting backwards.
     */
    public void validateIso8601Date(String dateString)
    {
        LocalDate date;
        // First, verify it's legit
        try {
            date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException dte) {
            throw new InvalidDateException("Not a valid ISO 8601 date");
        }

        // Next, limit the year range
        final int MAX_YEARS = 100;
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int minYear = currentYear - MAX_YEARS;

        int inputYear = date.getYear();
        if (inputYear < minYear || inputYear > currentYear) {
            throw new InvalidDateException("Year is outside of range.");
        }


    }
}
