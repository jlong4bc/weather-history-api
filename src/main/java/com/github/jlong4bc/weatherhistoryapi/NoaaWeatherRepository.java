package com.github.jlong4bc.weatherhistoryapi;

import com.github.jlong4bc.weatherhistoryapi.exception.InternalServerException;
import com.github.jlong4bc.weatherhistoryapi.exception.NoaaWeatherNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

        List<NoaaWeather> results = new ArrayList<>();

        final String[] HEADERS;

        BufferedReader bReader;
        try {
            InputStream iStream = con.getInputStream();
            int available = iStream.available();
            InputStreamReader isReader = new InputStreamReader(iStream);
            bReader = new BufferedReader(isReader);
            // set the number of characters that can be read from the stream to reserve the ability to reset to the beginning of the stream.
            bReader.mark(available);
            HEADERS = getHeader(bReader);
        } catch(IOException ioe) {
            throw new InternalServerException("Could not read weather.");
        }

        // Read from the stream again.
        try {
            bReader.reset();
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader(HEADERS)
                    .setSkipHeaderRecord(true).get();
            Iterable<CSVRecord> records = csvFormat.parse(bReader);
            for (CSVRecord record : records) {
                String dateStr = record.get("DATE");
                LocalDate localDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
                if ((localDate.isAfter(fromDate) && localDate.isBefore(toDate))
                    || localDate.isEqual(fromDate) || localDate.isEqual(toDate)) {
                    log.info("----> {}", record.stream().toList());

                    String highTempStr = record.get("TMAX");
                    String lowTempStr = record.get("TMIN");
                    String precipStr = record.get("PRCP");
                    String rain = record.isMapped("WT16") ? record.get("WT16") : "not-mapped";
                    String snow = record.isMapped("WT18") ? record.get("WT18") : "not-mapped";
                    String hail = record.isMapped("WT05") ? record.get("WT05") : "not-mapped";
                    String sleet = record.isMapped("WT04") ? record.get("WT04") : "not-mapped";
                    log.info("rain: {}, snow: {}, hail: {}, sleet: {}", rain, snow, hail, sleet);
                    record.toMap().forEach((key,value) -> log.info("{} -> {}", key, value));

                    NoaaWeather noaaWeather = new NoaaWeather();
                    noaaWeather.setDate(localDate);
                    noaaWeather.setHighTemp(getTemperatureFromStrTemperature(highTempStr));
                    noaaWeather.setLowTemp(getTemperatureFromStrTemperature(lowTempStr));
                    noaaWeather.setPrecipitationAmount(getPrecipFromStrPrecip(precipStr));
                    results.add(noaaWeather);
                }
            }
        } catch (IOException ex) {
            log.error("retrieving the CSV", ex);
        } finally {
            // Guarantee to close the reader
            try {
                bReader.close();
            } catch (IOException ex) {
                log.error("error closing the reader", ex);
            }
        }


        return results;
    }

    // Retrieve the Headers
    private String[] getHeader(Reader reader)
    {
        // Read headers because they may vary according to the data collected
        String[] HEADERS;

        // The header data retrieved may vary, which means the headers will vary.
        // We always hope the high & low temp along with precip will always be available.
        try {
            LineNumberReader lReader = new LineNumberReader(reader);
            String headerLine = lReader.readLine();
            // Remove all the quotes to enable proper split into String[]
            headerLine = headerLine.replace("\"", "");
            HEADERS = headerLine.split(",");
        } catch (IOException e) {
            throw new InternalServerException("Could not read weather header");
        }
        return HEADERS;
    }

    // Pre: Unit of measure : Celsius
    // Post: Unit of measure : Fahrenheit
    private static double getTemperatureFromStrTemperature(String str)
    {
        // NOAA temperature defaults to Celsius and include tenths without the decimal
        // Right now, have it return as Fahrenheit.  This can be modified later to be passed in as a parameter.
        double result = 0;
        try {
            int tempToTenths = Integer.parseInt(StringUtils.trim(str));
            double celsiusTemp = (double)tempToTenths/10;
            double fahrenheitTemp = (celsiusTemp * 9.0)/5.0 + 32;
            result = BigDecimal.valueOf(fahrenheitTemp).setScale(3, RoundingMode.HALF_UP).doubleValue();
        } catch(NumberFormatException ex) {
            // Do nothing
        }
        return result;
    }

    // Pre: Unit of measure : mm
    // Post: Unit of measure : in
    private static double getPrecipFromStrPrecip(String str)
    {
        // I am not sure exactly how to calculate this, but the documentation says,
        // "mm or inches as per user preference, inches to hundredths on Daily Form pdf file".
        // NOAA precp defaults to millimeters and include hundredths without the decimal
        // Right now, have it return as inches.  This can be modified later to be passed in as a parameter.
        double result = 0;
        try {
            int precipMMToHundredths = Integer.parseInt(StringUtils.trim(str));
            double precipIN = (precipMMToHundredths/254.0);
            result = BigDecimal.valueOf(precipIN).setScale(3, RoundingMode.HALF_UP).doubleValue();
        } catch(NumberFormatException ex) {
            // Do nothing
        }
        return result;
    }
}
