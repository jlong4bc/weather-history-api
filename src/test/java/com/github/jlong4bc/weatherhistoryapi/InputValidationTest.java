package com.github.jlong4bc.weatherhistoryapi;

import com.github.jlong4bc.weatherhistoryapi.exception.CountryNotFoundException;
import com.github.jlong4bc.weatherhistoryapi.exception.InvalidCityException;
import com.github.jlong4bc.weatherhistoryapi.exception.InvalidDateException;
import com.github.jlong4bc.weatherhistoryapi.exception.StateProvinceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Provides unit test coverage for the InputValidation class
 */
public class InputValidationTest
{
    private InputValidation inputValidation;

    @BeforeEach
    void setUp() {
        inputValidation = new InputValidation();
    }

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
            assertDoesNotThrow(() -> inputValidation.validateCountry(countryName));
        } else {
            assertThrows(CountryNotFoundException.class,
                    () -> inputValidation.validateCountry(countryName));
        }
    }

    @ParameterizedTest
    @CsvSource(value = {
            "Georgia,true",
            "California,false",
            "Tennessee,true",
            "Mars,false",
            "null,false",
            ",false"
    })
    void test_validateStateProvince(String stateProvinceName, boolean isLegit)
    {
        if (!isLegit) {
            assertDoesNotThrow(() -> inputValidation.validateStateProvince(stateProvinceName));
        } else {
            assertThrows(StateProvinceNotFoundException.class,
                    () -> inputValidation.validateStateProvince(stateProvinceName));
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
            assertDoesNotThrow(() -> inputValidation.validateCity(cityName));
        } else {
            assertThrows(InvalidCityException.class,
                    () -> inputValidation.validateCity(cityName));
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
            assertDoesNotThrow(() -> inputValidation.validateIso8601Date(dateString));
        } else {
            assertThrows(InvalidDateException.class,
                    () -> inputValidation.validateIso8601Date(dateString));
        }
    }

}
