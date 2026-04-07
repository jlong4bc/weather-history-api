package com.github.jlong4bc.weatherhistoryapi.elements;

import lombok.Getter;

/**
 *
 */
@Getter
public enum PrecipitationUoM
{
    METER("meter"),
    CENTIMETER("centimeter"),
    FEET("feet"),
    INCH("inch");

    public final String name;

    PrecipitationUoM(String name) {
        this.name = name;
    }

    public static PrecipitationUoM of(String name)
    {
        for (PrecipitationUoM uom : PrecipitationUoM.values()) {
            if (uom.getName().equals(name)) {
                return uom;
            }
        }
        return null;
    }
}
