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
        // Forgive the user for improper case
        for (StateProvince sp : StateProvince.values()) {
            if (sp.getProvinceName().equalsIgnoreCase(name)) {
                return sp;
            }
        }
        return null;
    }
}
