package com.github.jlong4bc.weatherhistoryapi;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A class to facilitate WeatherHistory retrieval from the backend.
 */
@AllArgsConstructor
@Service
public class WeatherService
{
    private final GeoCoordinateRepository geoRepo;
    private final NoaaStationRepository stationRepo;
    private final NoaaWeatherRepository weatherRepo;

    public List<WeatherHistory> retrieveWeatherHistory(InputData inputData)
    {
        LatLongBoundingBox latLongBb
                = geoRepo.getLatLongBoundingBox(inputData.country(), inputData.stateProvince(), inputData.city());

        String stationId = stationRepo.getStationId(latLongBb, inputData);
        
        List<WeatherHistory> wHistoryList = new ArrayList<>();
        
        //NoaaWeather noaaWeather = weatherRepo.getWeather(stationId, )


        return Collections.emptyList();
    }

}
