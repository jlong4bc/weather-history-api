package com.github.jlong4bc.weatherhistoryapi;

import com.github.jlong4bc.weatherhistoryapi.exception.ExternalApiException;
import com.github.jlong4bc.weatherhistoryapi.exception.InternalServerException;
import com.github.jlong4bc.weatherhistoryapi.exception.NoaaStationNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

/**
 * A class used to find NOAA stations
 */
@Repository
public class NoaaStationRepository
{
    private static final String API_URL = "https://www.ncdc.noaa.gov/cdo-web/api/v2/stations";

    private final RestClient restClient;


    public NoaaStationRepository(RestClient.Builder builder) {
        this.restClient = builder.baseUrl(API_URL)
                                 .build();
    }

    public String getStationId(LatLongBoundingBox latLongBoundingBox, InputData inData)
    {
        List<Station> stations;
        try {
            stations = callNoaaStationApi(latLongBoundingBox, inData.fromDate(), inData.toDate(), inData.noaaToken());
        } catch (HttpServerErrorException ex) {
            throw new ExternalApiException(ex.getMessage());
        } catch (NoaaStationNotFoundException ex) {
            // send it on without processing
            throw ex;
        } catch (Exception ex) {
            throw new InternalServerException(ex.getMessage());
        }

        // Arbitrarily select the first station name that matches the city & make sure it's from the state/province specified.
        // Edge cases for testing are Kansas City, MO & KS or Bristol, TN & VA
        String city = inData.city();
        String state = inData.stateProvince().name();

        Predicate<Station> isCity = station -> station.getName().startsWith(city.toUpperCase());
        Predicate<Station> containsState = station -> station.getName().contains(" "+state+" ");

        Station station = stations.stream().filter(isCity.and(containsState)).findFirst().orElse(null);
        if (station == null) {
            throw new NoaaStationNotFoundException("A NOAA weather station could not be found for the specified city and state.");
        }

        return station.getId();
    }

    private List<Station> callNoaaStationApi(LatLongBoundingBox bBox,
                                             LocalDate fromDate,
                                             LocalDate toDate,
                                             String noaaToken)
    {
        String startDateStr = fromDate.format(DateTimeFormatter.ISO_DATE);
        String endDateStr = toDate.format(DateTimeFormatter.ISO_DATE);

        StationApiWrapper wrapper =
                restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("extent",bBox.south()+","+bBox.west()+","+bBox.north()+","+bBox.east())
                        .queryParam("datasetid","GHCND")
                        .queryParam("datacategoryid","TEMP")
                        .queryParam("datacategoryid","PRCP")
                        .queryParam("startdate", startDateStr)
                        .queryParam("enddate", endDateStr)
                        .queryParam("limit", "50")
                        .build())
                .header("token", noaaToken)
                .header("Content-Type", "application/json")
                .retrieve()
                .body(StationApiWrapper.class);

        // Guard against not finding a station and throw an appropriate error.
        if (wrapper == null || wrapper.getResults() == null) {
            String msg = "A NOAA weather station could not be located for the date requested. Try a larger city or different date.";
            throw new NoaaStationNotFoundException(msg);
        }

        return wrapper.getResults();
    }

    /**
     * This represents the root of the JSON returned from the NOAA station API
     */
    @Getter
    @Setter
    public static class StationApiWrapper
    {
        private StationApiMetadata metadata;
        private List<Station> results;
    }

    /**
     * One of several objects in the NOAA API return for station
     */
    @Getter
    @Setter
    public static class StationApiMetadata
    {
        private StationApiResultSet resultSet;
    }

    /**
     * This class represents the station array metadata.
     */
    @Getter
    @Setter
    public static class StationApiResultSet
    {
        private int offset;
        private int count;
        private int limit;
    }

    /**
     * Temporary to capture station data
     */
    @Getter
    @Setter
    public static class Station
    {
        private int elevation;
        private String mindate;
        private String maxdate;
        private float latitude;
        private String name;
        private float datacoverage;
        private String id;
        private String elevationUnit;
        private float longitude;
    }
}
