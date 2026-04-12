package com.github.jlong4bc.weatherhistoryapi;

import com.github.jlong4bc.weatherhistoryapi.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides unit test coverage for the InputValidation class
 */
@Slf4j
public class InputValidationTest
{
    @ParameterizedTest
    @CsvSource(value = {
            "USA,true",
            "<script>,false",
            "drop table,false",
            "Canada, false",
            "null,false",
            ",false"
    })
    void test_validateCountry(String countryName, boolean isLegit)
    {
        if (isLegit) {
            assertDoesNotThrow(() -> InputValidation.validateCountry(countryName));
        } else {
            assertThrows(CountryNotFoundException.class,
                    () -> InputValidation.validateCountry(countryName));
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Georgia,true",
            "California,false",
            "Tennessee,true",
            "Mars,false",
            "null,false",
            ",false",
            "GA,true",
            "TN,true",
            "CA,false"
    })
    void test_validateStateProvince(String stateProvinceName, boolean isLegit)
    {
        if (isLegit) {
            assertDoesNotThrow(() -> InputValidation.validateStateProvince(stateProvinceName));
        } else {
            assertThrows(StateProvinceNotFoundException.class,
                    () -> InputValidation.validateStateProvince(stateProvinceName));
        }
    }

    @ParameterizedTest
    @CsvSource(value ={
            "Rancho Santa Margarita,true",
            "O'Neil,true",
            "#Dallas,false",
            "Tuscan>,false",
            "Kennesaw,true",
            "Eval,true",
            "Winchester-on-the-Severn,true",
            "<script>alert('hi')</script>, false"
    })
    void test_validateCity(String cityName, boolean isLegit)
    {
        if (isLegit) {
            assertDoesNotThrow(() -> InputValidation.validateCity(cityName));
        } else {
            assertThrows(InvalidCityException.class,
                    () -> InputValidation.validateCity(cityName));
        }
    }

    // Validates ISO 8601 formatted dates and validates specific year range
    @ParameterizedTest
    @CsvSource(value = {
            "1990-03-01,true",
            "2076-12-31,false",
            "1932-14-01,false",
            "1899-07-22,false",
            "1932-10-31,true",
            "1900-01-01,false"
    })
    void test_validateDateString(String dateString, boolean isLegit)
    {
        if (isLegit) {
            assertDoesNotThrow(() -> InputValidation.validateIso8601Date(dateString));
        } else {
            assertThrows(InvalidDateException.class,
                    () -> InputValidation.validateIso8601Date(dateString));
        }
    }

    // Validates the date range limit of date input
    @ParameterizedTest
    @CsvSource(value = {
            "1993-01-01,1993-01-04,true",
            "2022-04-20,2022-04-25,true",
            "2026-03-14,2026-03-20,false",
            "2020-07-01,2020-06-30,false",
            "2026-04-09,2026-04-09,true",
            "null,2026-04-09,false",
            "2026-04-09,null,false",
            "null,null,true"
    }, nullValues = "null")
    void test_validateDateRange(String fromDateStr, String toDateStr, boolean isLegit)
    {
        LocalDate fromDate = StringUtils.isEmpty(fromDateStr) ? null : LocalDate.parse(fromDateStr, DateTimeFormatter.ISO_DATE);
        LocalDate toDate = StringUtils.isEmpty(toDateStr) ? null : LocalDate.parse(toDateStr, DateTimeFormatter.ISO_DATE);

        if (isLegit) {
            assertDoesNotThrow(() -> InputValidation.validateDateRange(fromDate, toDate));
        } else {
            assertThrows(DateRangeException.class,
                    () -> InputValidation.validateDateRange(fromDate, toDate));
        }
    }
}
