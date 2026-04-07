package com.github.jlong4bc.weatherhistoryapi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.jlong4bc.weatherhistoryapi.elements.Precipitation;
import com.github.jlong4bc.weatherhistoryapi.elements.Temperature;

import java.time.LocalDate;

/**
 * A record of the weather in a location on a certain day.
 */
public record WeatherHistory(Country country, StateProvince stateProvince, String city,
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDate date,
                             Temperature temperature, Precipitation precipitation)
{
}
