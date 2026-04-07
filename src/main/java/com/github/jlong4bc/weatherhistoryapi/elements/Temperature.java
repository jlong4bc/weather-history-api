package com.github.jlong4bc.weatherhistoryapi.elements;

/*
 * Name:        Jason Long
 * Class:       CS 5040/Section#W01
 * Term:        Fall 2025
 * Instructor:  Umama Tasnim
 * Assignment:
 * IDE:         IntelliJ
 */

/**
 * A record to group all attributes of temperature
 */
public record Temperature(int high, int low, TemperatureUoM unitOfMeasure)
{
}
