package com.github.jlong4bc.weatherhistoryapi;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class provides integrated test coverage for the GeoCoordinateRepository.
 * This is an integration test use to identify transmission errors to and from the external API.
 * It provided great assistance to verify the client code was properly written and all values were in place.
 */
@Disabled("Because the assignment specifies no integration tests")
@Slf4j
@SpringBootTest
class GeoCoordinateRepoByOpenStreetIT
{
    @Autowired
    private GeoCoordinateRepository geoRepo;

    @BeforeEach
    void setUp() {
        assertNotNull(geoRepo,"geoRepo was not initialized.");
    }

    // Tests the ability to extract a LatLongBounding box from the repository.
    @Test
    void getLatLongBoundingBox()
    {
        LatLongBoundingBox llBBox = geoRepo.getLatLongBoundingBox(Country.USA, StateProvince.TN, "Chattanooga");
        assertNotNull(llBBox);
        log.info("llBBox: {}", llBBox);
    }
}