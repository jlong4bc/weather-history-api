package com.github.jlong4bc.weatherhistoryapi.elements;


/**
 * A record to group all attributes of temperature
 */
public record Temperature(int high, int low, TemperatureUoM unitOfMeasure)
{
}
