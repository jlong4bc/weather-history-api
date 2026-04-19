package com.github.jlong4bc.weatherhistoryapi;

import lombok.Getter;

/**
 * Represents a State or Province
 */
@Getter
public enum StateProvince
{
    GA("Georgia"),
    TN("Tennessee");

    private final String provinceName;

    StateProvince(String provinceName) {
        this.provinceName = provinceName;
    }

    // Find the enum given its name
    public static StateProvince of(String name)
    {
        try {
            return StateProvince.valueOf(name);
        } catch (IllegalArgumentException _) {
            // dismiss exception
        }

        // Try retrieving the name of the state / province instead of abbreviation.
        for (StateProvince sp : StateProvince.values()) {
            if (sp.getProvinceName().equalsIgnoreCase(name)) {
                return sp;
            }
        }

        return null;
    }
}
