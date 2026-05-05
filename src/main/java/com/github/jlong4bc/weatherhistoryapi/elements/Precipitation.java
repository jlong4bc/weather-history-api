package com.github.jlong4bc.weatherhistoryapi.elements;

/**
 * A record to group items of precipitation
 */
public record Precipitation(double amount, PrecipitationUoM unitOfMeasure, PrecipitationType type)
{
}
