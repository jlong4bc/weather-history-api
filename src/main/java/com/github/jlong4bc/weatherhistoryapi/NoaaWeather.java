package com.github.jlong4bc.weatherhistoryapi;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Represents a row of data from the NOAA CSV that has the past weather data.
 */
@Getter
@Setter
public class NoaaWeather
{
    private LocalDate date;
    private int highTemp;
    private int lowTemp;
    private int precipitationAmount;
}
