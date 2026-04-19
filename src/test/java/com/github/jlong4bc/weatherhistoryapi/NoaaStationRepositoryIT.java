package com.github.jlong4bc.weatherhistoryapi;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A class to test finding a NOAA weather station.
 * This is an integration test used to identify transmission errors to and from the external API.
 * It provided great assistance to verify the client code was properly written and all values were in place.
 */
@Disabled("Because the assignment specifies no integration tests")
@Slf4j
@SpringBootTest
class NoaaStationRepositoryIT
{
    @Autowired
    private NoaaStationRepository repo;
    private LatLongBoundingBox latLongBoundingBox;
    private InputData inputData;

    @BeforeEach
    void setUp()
    {
        latLongBoundingBox = new LatLongBoundingBox(34.9829,-85.4255,35.2143,-85.0515);

        LocalDate fromDate = LocalDate.parse("2024-01-01", DateTimeFormatter.ISO_DATE);
        LocalDate toDate = LocalDate.parse("2024-01-01", DateTimeFormatter.ISO_DATE);

        String noaaToken = System.getProperty("noaaToken");
        if (StringUtils.isEmpty(noaaToken)) {
            noaaToken = System.getenv("NOAA_TOKEN");
        }

        assertNotNull(noaaToken, "noaaToken must be set as a JVM argument or as an environment variable.");

        inputData = new InputData(Country.USA, StateProvince.TN, "Chattanooga", fromDate, toDate, noaaToken);

        log.info("inputData: {}", inputData);
    }

    // Tests the happy path when retrieving the stationId.
    @Test
    void test_getStationId()
    {
        String stationId = repo.getStationId(latLongBoundingBox, inputData);
        log.info("stationId: {}", stationId);

        assertNotNull(stationId, "A non-null value for stationId is expected.");

        //https://www.ncei.noaa.gov/data/daily-summaries/access/US1TNHL0002.csv
        //https://www.ncei.noaa.gov/data/daily-summaries/access/US1TNHL0031.csv
        //https://www.ncei.noaa.gov/data/daily-summaries/access/USW00013882.csv
    }
}