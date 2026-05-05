package com.github.jlong4bc.weatherhistoryapi;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

/**
 * Represents a row of data from the NOAA CSV that has the past weather data.
 */
@ToString
@Getter
@Setter
public class NoaaWeather
{
    private LocalDate date;
    private double highTemp;
    private double lowTemp;
    private double precipitationAmount;
}
