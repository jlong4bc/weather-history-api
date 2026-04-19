package com.github.jlong4bc.weatherhistoryapi;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Represents a library to retrieve weather history from NOAA.
 */
@Repository
public class NoaaWeatherRepository
{
    public NoaaWeather getWeather(String stationId, LocalDate date)
    {
        List<CsvBean> csvBeans = callNoaaFileServer(stationId, date);
        // Do stuff here to convert from the csv to the NoaaWeather object (or WeatherHistory???)
        return new NoaaWeather();
    }

    private List<CsvBean> callNoaaFileServer(String stationId, LocalDate date)
    {
        // Just call the file server to retrieve the data
        return List.of(new CsvBean());
    }

    /**
     * Temporary class to capture the rows of CSV data
     */
    public static class CsvBean
    {

    }


}
