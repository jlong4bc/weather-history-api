package com.github.jlong4bc.weatherhistoryapi;

import com.github.jlong4bc.weatherhistoryapi.elements.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        
        List<NoaaWeather> noaaWeatherResults = weatherRepo.getWeather(stationId, inputData.fromDate(), inputData.toDate());

        for (NoaaWeather noaaWeather : noaaWeatherResults) {

            Temperature temp = new Temperature(noaaWeather.getHighTemp(), noaaWeather.getLowTemp(), TemperatureUoM.CELSIUS);
            Precipitation precip = new Precipitation(noaaWeather.getPrecipitationAmount(), PrecipitationUoM.CENTIMETER, PrecipitationType.UNKNOWN);
            WeatherHistory wHistory = new WeatherHistory(inputData.country(), inputData.stateProvince(),
                                                         inputData.city(), noaaWeather.getDate(), temp, precip);
            wHistoryList.add(wHistory);
        }


        return wHistoryList;
    }

}
