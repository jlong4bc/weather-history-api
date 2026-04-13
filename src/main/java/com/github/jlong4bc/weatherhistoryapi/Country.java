package com.github.jlong4bc.weatherhistoryapi;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents a country.
 */
@Getter
public enum Country
{
    USA("USA");

    private final String countryName;

    Country(String countryName) {
        this.countryName = countryName;
    }

    // Find the country given its name
    public static Country of(String name)
    {
        if (StringUtils.isEmpty(name)) {
            return null;
        }

        // Forgive the user for improper case
        for (Country country : Country.values()) {
            if (country.getCountryName().equalsIgnoreCase(name)) {
                return country;
            }
        }
        return null;
    }
}
