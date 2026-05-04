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

    private final static String stationId = "USW00013882";
    private final NoaaWeatherRepository repo = new NoaaWeatherRepository();

    @Test
    void getWeather()
    {
        LocalDate from = LocalDate.of(2026, 2, 5);
        LocalDate to = LocalDate.of(2026, 2, 5);
        List<NoaaWeather> weatherResults = repo.getWeather(stationId, from, to);
        assertNotNull(weatherResults, "noaaWeather should not be null");
        assertFalse(weatherResults.isEmpty(), "weatherResults should not be empty");
        log.info("weatherResults: {}", weatherResults);
    }
}