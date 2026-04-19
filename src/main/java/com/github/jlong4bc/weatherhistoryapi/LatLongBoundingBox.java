package com.github.jlong4bc.weatherhistoryapi;

/**
 * This class describes a rectangular area that use latitude and longitude to identify its corners
 */
public record LatLongBoundingBox(double south, double west, double north, double east) {}
