package com.github.jlong4bc.weatherhistoryapi.elements;

/*
 * Name:        Jason Long
 * Class:       CS 5040/Section#W01
 * Term:        Fall 2025
 * Instructor:  Umama Tasnim
 * Assignment:
 * IDE:         IntelliJ
 */

import lombok.Getter;

/**
 *
 */
@Getter
public enum PrecipitationType
{
    SNOW("snow"),
    SLEET("sleet"),
    RAIN("rain"),
    HAIL("hail");

    private final String name;

    PrecipitationType(String name) {
        this.name = name;
    }

    public static PrecipitationType of(String name)
    {
        for (PrecipitationType precip : PrecipitationType.values()) {
            if (precip.getName().equals(name)) {
                return precip;
            }
        }
        return null;
    }
}
