package com.github.jlong4bc.weatherhistoryapi;

import com.github.jlong4bc.weatherhistoryapi.exception.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * A class that validates raw input from the user to ensure it's not harmful for processing
 * and to inform the user of input error.
 */
public final class InputValidation
{
    private InputValidation() {
        // Private to prevent creation because all methods are static.
    }

    // Focus on allowed characters like a space, apostrophe and "A" through "z";
    // Allow no more than 25 characters in a city name which allows for most city names
    private static final Pattern VALID_CITY_PATTERN = Pattern.compile("([\\s|'A-z-]){0,25}");

    private static final int DAY_RANGE = 5;


    /**
     * Provides country name validity
     * @param countryName the country name to validate
     */
    public static void validateCountry(String countryName)
    {
        if (countryName == null) {
            throw new CountryNotFoundException("Country must be specified.");
        }

        // Only allow countries that can be processed.
        if (Country.of(countryName) == null) {
            throw new CountryNotFoundException();
        }
    }

    /**
     * Provides state or province abbreviation validity
     * @param stateProvinceAbbr the state or province to validate
     */
    public static void validateStateProvince(String stateProvinceAbbr)
    {
        if (StringUtils.isEmpty(stateProvinceAbbr)) {
            throw new StateProvinceNotFoundException("State / Province must be specified.");
        }

        // First, check for an abbreviation
        try {
            StateProvince.valueOf(stateProvinceAbbr);
        } catch (IllegalArgumentException _) {
            // Second, check for the actual name
            if (StateProvince.of(stateProvinceAbbr) == null) {
                throw new StateProvinceNotFoundException();
            }
        }
    }

    /**
     * Provides basic validation of alphabetic characters and typical characters in proper names
     * @param cityName the city to validate
     */
    public static void validateCity(String cityName)
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
    public static void validateIso8601Date(String dateString)
    {
        LocalDate date;
        // First, verify it's legit
        try {
            date = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException _) {
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

    /**
     * Validates the date range of the from and to date parameters
     * @param fromDate a date less than the toDate
     * @param toDate a date greater than the fromDate
     */
    public static void validateDateRange(LocalDate fromDate, LocalDate toDate)
    {
        // Both from & to dates are optional inputs
        if (fromDate == null && toDate == null) {
            return;
        }

        // When one date is given and another is not, throw an exception because of the ambiguity
        if (fromDate == null || toDate == null) {
            throw new DateRangeException("Ambiguous input: 'from' date and 'to' date must both have values or both be absent");
        }

        if (fromDate.isAfter(toDate)) {
            throw new DateRangeException("'From' date must be less than 'To' date.");
        }

        if ((fromDate.plusDays(DAY_RANGE)).isBefore(toDate)) {
            throw new DateRangeException("No more than " + DAY_RANGE+ " days range is allowed.");
        }
    }

    /**
     * Provides a very rudimentary validation check because NOAA CDO does not provide specific how-tos
     * for verifying proper token structure.
     * @param token a NOAA CDO specific token
     */
    public static void validateTokenExists(String token)
    {
        if (StringUtils.isEmpty(token)) {
            throw new TokenNotFoundException();
        }
    }
}
