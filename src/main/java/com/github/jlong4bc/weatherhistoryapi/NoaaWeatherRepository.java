package com.github.jlong4bc.weatherhistoryapi;

import com.github.jlong4bc.weatherhistoryapi.exception.NoaaWeatherNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library to retrieve weather history from NOAA.
 */
@Slf4j
@Repository
public class NoaaWeatherRepository
{
    public List<NoaaWeather> getWeather(String stationId, LocalDate fromDate, LocalDate toDate)
    {
        stationId = stationId.replace("GHCND:", "");
        return callNoaaFileServer(stationId, fromDate, toDate);
    }

    // A method to retrieve historical weather data from NOAA's file server
    private List<NoaaWeather> callNoaaFileServer(String stationId, LocalDate fromDate, LocalDate toDate)
    {
        // Just call the file server to retrieve the data
        String urlStr = String.format("https://www.ncei.noaa.gov/data/daily-summaries/access/%s.csv", stationId);

        URL url;
        URLConnection con;
        try {
            url = URI.create(urlStr).toURL();
            con = url.openConnection();
        } catch(MalformedURLException ex) {
            throw new NoaaWeatherNotFoundException("Weather station is not valid.");
        } catch (IOException ex) {
            throw new NoaaWeatherNotFoundException("Could not connect to NOAA weather archive.");
        }

        final String[] HEADERS = {"STATION","DATE","LATITUDE","LONGITUDE","ELEVATION","NAME","PRCP","PRCP_ATTRIBUTES","SNOW","SNOW_ATTRIBUTES","SNWD","SNWD_ATTRIBUTES","TMAX","TMAX_ATTRIBUTES","TMIN","TMIN_ATTRIBUTES","ACMH","ACMH_ATTRIBUTES","ACSH","ACSH_ATTRIBUTES","ADPT","ADPT_ATTRIBUTES","ASLP","ASLP_ATTRIBUTES","ASTP","ASTP_ATTRIBUTES","AWBT","AWBT_ATTRIBUTES","AWND","AWND_ATTRIBUTES","FMTM","FMTM_ATTRIBUTES","FRGB","FRGB_ATTRIBUTES","FRGT","FRGT_ATTRIBUTES","FRTH","FRTH_ATTRIBUTES","GAHT","GAHT_ATTRIBUTES","PGTM","PGTM_ATTRIBUTES","PSUN","PSUN_ATTRIBUTES","RHAV","RHAV_ATTRIBUTES","RHMN","RHMN_ATTRIBUTES","RHMX","RHMX_ATTRIBUTES","TAVG","TAVG_ATTRIBUTES","TOBS","TOBS_ATTRIBUTES","TSUN","TSUN_ATTRIBUTES","WDF1","WDF1_ATTRIBUTES","WDF2","WDF2_ATTRIBUTES","WDF5","WDF5_ATTRIBUTES","WDFG","WDFG_ATTRIBUTES","WDFM","WDFM_ATTRIBUTES","WESD","WESD_ATTRIBUTES","WSF1","WSF1_ATTRIBUTES","WSF2","WSF2_ATTRIBUTES","WSF5","WSF5_ATTRIBUTES","WSFG","WSFG_ATTRIBUTES","WSFM","WSFM_ATTRIBUTES","WT01","WT01_ATTRIBUTES","WT02","WT02_ATTRIBUTES","WT03","WT03_ATTRIBUTES","WT04","WT04_ATTRIBUTES","WT05","WT05_ATTRIBUTES","WT06","WT06_ATTRIBUTES","WT07","WT07_ATTRIBUTES","WT08","WT08_ATTRIBUTES","WT09","WT09_ATTRIBUTES","WT10","WT10_ATTRIBUTES","WT11","WT11_ATTRIBUTES","WT13","WT13_ATTRIBUTES","WT14","WT14_ATTRIBUTES","WT15","WT15_ATTRIBUTES","WT16","WT16_ATTRIBUTES","WT17","WT17_ATTRIBUTES","WT18","WT18_ATTRIBUTES","WT19","WT19_ATTRIBUTES","WT21","WT21_ATTRIBUTES","WT22","WT22_ATTRIBUTES","WV03","WV03_ATTRIBUTES"};

        List<NoaaWeather> results = new ArrayList<>();

        try (Reader reader = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true).get();
            Iterable<CSVRecord> records = csvFormat.parse(reader);
            for (CSVRecord record : records) {
                String dateStr = record.get("DATE");
                LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
                if ((localDate.isAfter(fromDate) && localDate.isBefore(toDate))
                    || localDate.isEqual(fromDate) || localDate.isEqual(toDate)) {
                    log.info("----> {}", record.stream().toList());

                    String highTempStr = record.get("TMAX");
                    String lowTempStr = record.get("TMIN");
                    String precipStr = record.get("PRCP");

                    NoaaWeather noaaWeather = new NoaaWeather();
                    noaaWeather.setDate(localDate);
                    noaaWeather.setHighTemp(getIntFromStr(highTempStr));
                    noaaWeather.setLowTemp(getIntFromStr(lowTempStr));
                    noaaWeather.setPrecipitationAmount(getIntFromStr(precipStr));
                    results.add(noaaWeather);
                }
            }
        } catch (IOException ex) {
            log.error("retrieving the CSV", ex);
        }

        // call a private method to turn the bytes[] into an array of CsvBeans

        return results;
    }

    private static int getIntFromStr(String str)
    {
        int result = 0;
        try {
            result = Integer.parseInt(StringUtils.trim(str));
        } catch(NumberFormatException ex) {
            // Do nothing
        }
        return result;
    }
}
