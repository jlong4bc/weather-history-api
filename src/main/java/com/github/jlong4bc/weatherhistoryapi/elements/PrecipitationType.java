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
    HAIL("hail"),
    UNKNOWN("unknown");

    private final String typeName;

    PrecipitationType(String name) {
        this.typeName = name;
    }

    public static PrecipitationType of(String name)
    {
        try {
            return PrecipitationType.valueOf(name);
        } catch (IllegalArgumentException _) {
            // dismiss exception
        }

        // Try retrieving based on more lax rules
        for (PrecipitationType precip : PrecipitationType.values()) {
            if (precip.getTypeName().equalsIgnoreCase(name)) {
                return precip;
            }
        }
        return null;
    }
}
