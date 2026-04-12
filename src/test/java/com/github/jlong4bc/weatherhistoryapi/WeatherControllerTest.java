package com.github.jlong4bc.weatherhistoryapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.jlong4bc.weatherhistoryapi.elements.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class provides test coverage for the Controller.
 * Because it invokes the framework for injecting the mock service and converting object output into a JSON format, it
 * could be considered an integration test.
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest
class WeatherControllerTest
{
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;



    @ParameterizedTest
    @CsvSource(value={
            "USA,TN,Chattanooga,1993-03-13,1993-03-13,-7,-17,Celsius,300,centimeter,snow,1993-03-13,0987654321,true",
            //"USA,TN,Chattanooga,null,1993-03-13,-7,-17,Celsius,300,centimeter,snow,1993-03-13,0987654321,false",
            //"USA,TN,Chattanooga,1993-03-13,null,-7,-17,Celsius,300,centimeter,snow,1993-03-13,0987654321,false",
            "USA,TN,Chattanooga,null,null,-7,-17,Celsius,300,centimeter,snow,1993-03-13,0987654321,true",
    }, nullValues = {"null"})
    void test_retrieveWeatherHistory(String country, String stateProvince, String city,
                                     String fromDate, String toDate,
                                     int highTemp, int lowTemp, String tempUom,
                                     int precipAmt, String precipUom, String precipType,
                                     String iso8601Date, String token, boolean isLegit)
        throws Exception
    {
        // This is a mocked-up test to verify the controller endpoint to retrieve
        // weather history is defined properly

        // Build a return object and convert it to a JSON formatted string
        ObjectMapper om = new ObjectMapper();
        // Allow Jackson to parse Java 8 LocalDate by registering module
        om.registerModule(new JavaTimeModule());

        Temperature temp = buildTemperature(highTemp, lowTemp, tempUom);
        Precipitation precip = buildPrecipitation(precipAmt, precipType, precipUom);

        WeatherHistory wHistory = buildWeatherHistory(country, stateProvince, city,
                                                      iso8601Date, temp, precip);

        Mockito.when(weatherService.retrieveWeatherHistory(Mockito.any())).thenReturn(wHistory);

        // Allows full comparison of JSON
        String jsonResults = om.writeValueAsString(wHistory);

        ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
        if (!isLegit) {
            statusMatcher = MockMvcResultMatchers.status().is4xxClientError();
            jsonResults = "";
        }

        String uriTemplate = "/weather/{country}/{state-province}/{city}";

        mockMvc.perform(MockMvcRequestBuilders.get(uriTemplate, country, stateProvince, city)
                        .contentType(MediaType.APPLICATION_JSON)
                //.accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .param("fromDateParam", fromDate)
                .param("toDateParam",toDate)
        ).andExpect(statusMatcher)
         //.andExpect(MockMvcResultMatchers.jsonPath("$.city", Matchers.is(city))).andDo(MockMvcResultHandlers.print());
         .andExpect(MockMvcResultMatchers.content().json(jsonResults));
    }

    private WeatherHistory buildWeatherHistory(String countryName, String stateProvName, String city,
                                               String iso8601Date, Temperature temperature, Precipitation precipitation)
    {
        Country country = Country.of(countryName);
        StateProvince stateProv = StateProvince.of(stateProvName);
        LocalDate date = LocalDate.parse(iso8601Date, DateTimeFormatter.ISO_DATE);

        return new WeatherHistory(country, stateProv, city, date, temperature, precipitation);
    }

    private Temperature buildTemperature(int high, int low, String tempUomName)
    {
        TemperatureUoM tempUom = TemperatureUoM.of(tempUomName);
        return new Temperature(high, low, tempUom);
    }

    private Precipitation buildPrecipitation(int precipAmt, String precipName, String precipUomName)
    {
        PrecipitationType precipType = PrecipitationType.of(precipName);
        PrecipitationUoM precipUom = PrecipitationUoM.of(precipUomName);

        return new Precipitation(precipAmt, precipUom, precipType);
    }

}
