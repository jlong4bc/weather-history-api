package com.github.jlong4bc.weatherhistoryapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.jlong4bc.weatherhistoryapi.exception.ExternalApiException;
import com.github.jlong4bc.weatherhistoryapi.exception.GeoLocationNotFoundException;
import com.github.jlong4bc.weatherhistoryapi.exception.InternalServerException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * This implementation uses the OpenStreetMaps hosted [Nominatim](https://nominatim.org) to power their UI search.
 * [OpenStreet-Nominatim](https://wiki.openstreetmap.org/wiki/Nominatim)
 * This endpoint is not for heavy use (max of 1 request per second).
 * See [policy](https://operations.osmfoundation.org/policies/nominatim/).
 */
@Slf4j
@Repository
public class GeoCoordinateRepoByOpenStreet implements GeoCoordinateRepository
{
    // Created User-Agent that is necessary to programmatically call this particular API for identification.
    private static final String REQUIRED_USER_AGENT = "github - jlong4bc - weather-history-api";

    private static final String API_URL = "https://nominatim.openstreetmap.org/search";

    private final RestClient restClient;


    public GeoCoordinateRepoByOpenStreet(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(API_URL)
                                 .build();
    }

    @Override
    public LatLongBoundingBox getLatLongBoundingBox(Country country, StateProvince stateProvince, String city)
    {
        NominatimGeoLocation geoLocation;
        try {
            geoLocation = callGeoLocatorApi(country, stateProvince, city);
        } catch (HttpServerErrorException ex) {
            throw new ExternalApiException(ex.getMessage());
        } catch (GeoLocationNotFoundException ex) {
            // send it on without processing
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException(ex.getMessage());
        }

        return extractLatLongBoundingBoxFromGeoLocation(geoLocation);
    }

    private NominatimGeoLocation callGeoLocatorApi(Country country, StateProvince stateProvince, String city)
    {
        // Call Nominatim on OpenStreet
        List<NominatimGeoLocation> geoLocations =
            restClient.get()
                      .uri(uriBuilder -> uriBuilder
                              .queryParam("city", city)
                              .queryParam("state", stateProvince.name())
                              .queryParam("country", country.name())
                              .queryParam("format", "json")
                              .build())
                      .header("User-Agent", REQUIRED_USER_AGENT)
                      .header("Content-Type", "application/json")
                      .retrieve()
                      .body(new ParameterizedTypeReference<>() {});

        log.info("geoLocations : {}", geoLocations);

        if (geoLocations == null || geoLocations.isEmpty()) {
            throw new GeoLocationNotFoundException();
        }

        return geoLocations.getFirst();
    }

    // A helper method to isolate extracting the bounding box from the geolocation retrieved from the API call.
    private LatLongBoundingBox extractLatLongBoundingBoxFromGeoLocation(NominatimGeoLocation geoLocation)
    {
        // Follows standard minLat(south), maxLat(north), minLong(west), maxLong(east)
        String[] boundingBox = geoLocation.getBoundingBox();

        if (boundingBox == null || boundingBox.length < 4) {
            log.warn("The bounding box is not correct for geoLocation : {} ", geoLocation);
            throw new GeoLocationNotFoundException();
        }

        double south = convertToUsableCoordinate(boundingBox[0]);
        double north = convertToUsableCoordinate(boundingBox[1]);
        double west = convertToUsableCoordinate(boundingBox[2]);
        double east = convertToUsableCoordinate(boundingBox[3]);

        return new LatLongBoundingBox(south, west, north, east);
    }

    // A utility that converts a coordinateAsStr represented as a string to a double
    // that can be used by this application.
    private double convertToUsableCoordinate(String coordinateAsStr)
    {
        // The objective is to truncate the decimal part to four digits.
        double tempDouble = Double.parseDouble(coordinateAsStr);
        // Rounding down is effectively truncating.
        return BigDecimal.valueOf(tempDouble).setScale(4, RoundingMode.DOWN).doubleValue();
    }

    // An object representation of the JSON returned from the GeoLocation API.
    @ToString
    @Getter
    @Setter
    static class NominatimGeoLocation
    {
        // Using Jackson annotations to specify the expected name while ensuring standard Java naming conventions.
        @JsonProperty("place_id")
        private int      placeId;
        private String   licence;
        @JsonProperty("osm_type")
        private String   osmType;
        @JsonProperty("osm_id")
        private int      osmId;
        private String   lat;
        private String   lon;
        @JsonProperty("class")
        private String   className;
        private String   type;
        @JsonProperty("place_rank")
        private int      placeRank;
        private float    importance;
        @JsonProperty("addresstype")
        private String   addressType;
        private String   name;
        @JsonProperty("display_name")
        private String   displayName;
        @JsonProperty("boundingbox")
        private String[] boundingBox;
    }
}
