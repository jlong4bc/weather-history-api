package com.github.jlong4bc.weatherhistoryapi;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 *
 */
@Getter
public enum Country
{
    USA("USA");

    private final String name;

    Country(String name) {
        this.name = name;
    }

    public static Country of(String name)
    {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        for (Country country : Country.values()) {
            if (country.getName().equals(name)) {
                return country;
            }
        }
        return null;
    }
}
