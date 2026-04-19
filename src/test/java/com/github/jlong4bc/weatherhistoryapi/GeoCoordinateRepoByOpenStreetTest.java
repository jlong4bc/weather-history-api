package com.github.jlong4bc.weatherhistoryapi;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static com.github.jlong4bc.weatherhistoryapi.GeoCoordinateRepoByOpenStreet.NominatimGeoLocation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * This class provides unit test coverage for the GeoCoordinateRepository.
 */
@ExtendWith(MockitoExtension.class)
@RestClientTest(GeoCoordinateRepoByOpenStreet.class)
class GeoCoordinateRepoByOpenStreetTest
{
    @Autowired
    private MockRestServiceServer server;

    @Mock
    private NominatimGeoLocation geoLocation;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private GeoCoordinateRepository geoRepo;


    // The focus is verifying the private method, convertToUsableCoordinates, travels the happy path.
    @ParameterizedTest
    @CsvSource(value = {
            "34.9829380|34.9829|35.2143460|35.2143|-85.4255628|-85.4255|-85.0515870|-85.0515",
            "100.1234567|100.1234|9.2345678|9.2345|-99.1234567|-99.1234|0.1234567|0.1234"
    },delimiter = '|')
    void test_getLatLongBoundingBox(String south, String modifiedSouth,
                                    String north, String modifiedNorth,
                                    String west, String modifiedWest,
                                    String east, String modifiedEast)
    {
        // given
        // Follows standard minLat(south), maxLat(north), minLong(west), maxLong(east)
        String[] boundingBox = {south, north, west, east};
        Mockito.when(geoLocation.getBoundingBox()).thenReturn(boundingBox);

        // when
        this.server.expect(requestTo("https://nominatim.openstreetmap.org/search?city=Any&state=TN&country=USA&format=json"))
                .andRespond(withSuccess(objectMapper.writeValueAsString(List.of(geoLocation)), MediaType.APPLICATION_JSON));
        // then
        LatLongBoundingBox bBox = geoRepo.getLatLongBoundingBox(Country.USA, StateProvince.TN, "Any");
        assertNotNull(bBox, "bBox should not be null.");
        assertEquals(""+bBox.south(), modifiedSouth, "south - truncation did not work");
        assertEquals(""+bBox.north(), modifiedNorth, "north - truncation did not work");
        assertEquals(""+bBox.west(), modifiedWest, "west - truncation did not work");
        assertEquals(""+bBox.east(), modifiedEast, "east - truncation did not work");
    }
}