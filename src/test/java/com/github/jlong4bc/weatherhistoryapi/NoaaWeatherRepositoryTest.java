package com.github.jlong4bc.weatherhistoryapi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides integrated test coverage for NoaaWeatherRepository
 * (integrated because it accesses an external server which is very helpful when testing connections)
 */
@Slf4j
class NoaaWeatherRepositoryTest
{

    private static final String CHATTANOOGA_STATION_ID = "USW00013882";
    private static final String MARIETTA_STATION_ID = "USW00013864";
    private static final String stationId = MARIETTA_STATION_ID;
    private final NoaaWeatherRepository repo = new NoaaWeatherRepository();

    @Test
    void getWeather()
    {
        LocalDate from = LocalDate.of(2026, 4, 27);
        LocalDate to = LocalDate.of(2026, 4, 30);
        List<NoaaWeather> weatherResults = repo.getWeather(stationId, from, to);
        assertNotNull(weatherResults, "noaaWeather should not be null");
        assertFalse(weatherResults.isEmpty(), "weatherResults should not be empty");
        log.info("weatherResults: {}", weatherResults);
    }
}