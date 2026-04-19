package com.github.jlong4bc.weatherhistoryapi;


/**
 * Specifies an interface for implementations that retrieve geographical coordinates.
 * There are multiple geocoding APIs available for use, and some are for commercial use which is more
 * appropriate for heavy geocoding usage.
 */
public interface GeoCoordinateRepository
{
    LatLongBoundingBox getLatLongBoundingBox(Country country, StateProvince stateProvince, String city);

}
