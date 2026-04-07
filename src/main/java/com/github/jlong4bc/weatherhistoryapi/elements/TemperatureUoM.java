package com.github.jlong4bc.weatherhistoryapi.elements;

import lombok.Getter;

/**
 *
 */
@Getter
public enum TemperatureUoM
{
    CELSIUS("Celsius"),
    FAHRENHEIT("Fahrenheit");

    private final String name;

    TemperatureUoM(String name) {
        this.name = name;
    }

    public static TemperatureUoM of(String name)
    {
        for (TemperatureUoM uom : TemperatureUoM.values()) {
            if (uom.getName().equals(name)) {
                return uom;
            }
        }
        return null;
    }
}
