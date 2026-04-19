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

    private final String unitName;

    TemperatureUoM(String name) {
        this.unitName = name;
    }

    public static TemperatureUoM of(String name)
    {
        try {
            return TemperatureUoM.valueOf(name);
        } catch (IllegalArgumentException _) {
            // dismiss exception
        }

        // Try retrieving based on more lax rules.
        for (TemperatureUoM uom : TemperatureUoM.values()) {
            if (uom.getUnitName().equalsIgnoreCase(name)) {
                return uom;
            }
        }
        return null;
    }
}
