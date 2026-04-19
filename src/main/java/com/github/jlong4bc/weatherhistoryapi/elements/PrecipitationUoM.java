package com.github.jlong4bc.weatherhistoryapi.elements;

import lombok.Getter;

/**
 * Specifies the unit of measure for the precipitation.
 */
@Getter
public enum PrecipitationUoM
{
    METER("meter"),
    CENTIMETER("centimeter"),
    FEET("feet"),
    INCH("inch");

    public final String unitName;

    PrecipitationUoM(String unitName) {
        this.unitName = unitName;
    }

    public static PrecipitationUoM of(String name)
    {
        try {
            return PrecipitationUoM.valueOf(name);
        } catch (IllegalArgumentException _) {
            // dismiss exception
        }

        // Try retrieving based on more lax rules
        for (PrecipitationUoM uom : PrecipitationUoM.values()) {
            if (uom.getUnitName().equalsIgnoreCase(name)) {
                return uom;
            }
        }
        return null;
    }
}
