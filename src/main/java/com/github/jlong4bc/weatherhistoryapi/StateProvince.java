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

    private final String name;

    StateProvince(String name) {
        this.name = name;
    }

    public static StateProvince of(String name)
    {
        for (StateProvince sp : StateProvince.values()) {
            if (sp.getName().equals(name)) {
                return sp;
            }
        }
        return null;
    }
}
