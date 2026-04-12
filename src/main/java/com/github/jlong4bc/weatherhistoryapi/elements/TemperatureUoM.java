package com.github.jlong4bc.weatherhistoryapi.elements;

import lombok.Getter;

/**
 * Specifies the unit of measure for the temperature.
 */
@Getter
public enum TemperatureUoM
{
    CELSIUS("Celsius"),
    FAHRENHEIT("Fahrenheit");

    private final String description;

    TemperatureUoM(String name) {
        this.description = name;
    }

    public static TemperatureUoM of(String name)
    {
        for (TemperatureUoM uom : TemperatureUoM.values()) {
            if (uom.getDescription().equals(name)) {
                return uom;
            }
        }
        return null;
    }
}
