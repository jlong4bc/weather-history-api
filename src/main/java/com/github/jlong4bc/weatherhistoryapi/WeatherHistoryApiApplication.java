package com.github.jlong4bc.weatherhistoryapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class WeatherHistoryApiApplication {

    static void main(String[] args) {
        SpringApplication.run(WeatherHistoryApiApplication.class, args);
    }

    /**
     * Taken from Bhanu's Blog (see Acknowledgments in README)
     * This specifies that the Open API documentation create an input for Authentication bearer token.
     * Token format isn't important so that was not included.
     * @return An OpenAPI object to be used by the Open API dependencies to create the additional input.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-key", Collections.emptyList()));
    }

}
