package com.github.jlong4bc.weatherhistoryapi;

import java.time.LocalDate;

/**
 * A record of the user's validated data
 */
public record InputData(Country country, StateProvince stateProvince, String city,
                        LocalDate fromDate, LocalDate toDate, String noaaToken)
{
}
