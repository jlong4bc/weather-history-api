package com.github.jlong4bc.weatherhistoryapi;

/**
 * A record for the input data that stores unchanged user input.
 */
public record RawInputData(String countryName, String stateProvinceName, String cityName,
                           String fromDateStr, String toDateStr, String noaaToken)
{
}
