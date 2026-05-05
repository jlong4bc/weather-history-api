package com.github.jlong4bc.weatherhistoryapi.elements;


/**
 * A record to group all attributes of temperature
 */
public record Temperature(double high, double low, TemperatureUoM unitOfMeasure)
{
}
