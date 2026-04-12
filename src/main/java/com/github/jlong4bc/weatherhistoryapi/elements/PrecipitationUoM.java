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

    public final String description;

    PrecipitationUoM(String description) {
        this.description = description;
    }

    public static PrecipitationUoM of(String name)
    {
        for (PrecipitationUoM uom : PrecipitationUoM.values()) {
            if (uom.getDescription().equals(name)) {
                return uom;
            }
        }
        return null;
    }
}
