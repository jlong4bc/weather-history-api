package com.github.jlong4bc.weatherhistoryapi;

import lombok.Getter;

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
        try {
            return Country.valueOf(name);
        } catch (IllegalArgumentException _) {
            // dismiss exception
        }

        // Try retrieving based on the country name instead of abbreviation.
        for (Country country : Country.values()) {
            if (country.getCountryName().equalsIgnoreCase(name)) {
                return country;
            }
        }
        return null;
    }
}
