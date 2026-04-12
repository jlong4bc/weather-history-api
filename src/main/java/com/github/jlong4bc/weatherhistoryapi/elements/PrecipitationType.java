package com.github.jlong4bc.weatherhistoryapi.elements;


import lombok.Getter;

/**
 * Identifies the type of precipitation for a weather event.
 */
@Getter
public enum PrecipitationType
{
    SNOW("snow"),
    SLEET("sleet"),
    RAIN("rain"),
    HAIL("hail");

    private final String description;

    PrecipitationType(String name) {
        this.description = name;
    }

    public static PrecipitationType of(String name)
    {
        for (PrecipitationType precip : PrecipitationType.values()) {
            if (precip.getDescription().equals(name)) {
                return precip;
            }
        }
        return null;
    }
}
