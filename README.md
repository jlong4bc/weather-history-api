# Weather History API

<!-- TABLE OF CONTENTS -->
<details open>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
## About The Project
There are several sets of APIs provided by 
NOAA. An older version provides current forecast data directly through the APIs. Historical data is kept 
in CSV files that are named with location identifiers. A newer version NOAA maintains is more user friendly in 
some instances, but retrieving basic historical weather data is still complex. 
The goal of this project is to provide a more user-friendly set of APIs that leverage NOAA’s existing APIs to 
retrieve basic, location-specific, historical weather data. 


### Built With
Java version 25  
SpringBoot 4.0.4

<!-- GETTING STARTED -->
## Getting Started

### Prerequisites
- Java 25 
- Request a token from NOAA Climate Data Online 
  - https://www.ncdc.noaa.gov/cdo-web/token

### Installation
* Install Java - Using WSL2, *nix or Mac? Try [SDKMan!](https://sdkman.io/)
* Download this application


<!-- USAGE EXAMPLES -->
## Usage
* Start the application
```sh
./mvnw spring-boot:run
```
* Request from localhost
```sh
curl "http://localhost:8080/weather/USA/TN/Chattanooga?fromDate=1993-03-01&toDate=1993-03-31" -H "token:0987654321"
```

<!-- Running Tests -->
### Running Tests
```sh
./mvnw test -Dtest=ControllerTest#test_retrieveWeatherHistory
```

<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

* [NOAA - National Centers for Environmental Information](https://www.ncei.noaa.gov/)
* [OpenStreetMap](https://openstreetmap.org)
* [Best Readme Template](https://github.com/othneildrew/Best-README-Template/blob/main/BLANK_README.md#about-the-project)
* [Building an Application with Spring Boot](https://spring.io/guides/gs/spring-boot)
* [Spring Boot with TDD— Part I](https://medium.com/@sheikarbaz5/spring-boot-with-tdd-test-driven-development-part-i-be1b90da51e)
* [Baeldung - Spring PathVariable](https://www.baeldung.com/spring-pathvariable)
* [Baeldung - Spring QueryParameter](https://www.baeldung.com/spring-request-param)
* [Enhancing Logging with @Log and @Slf4j in Spring Boot Applications](https://medium.com/@AlexanderObregon/enhancing-logging-with-log-and-slf4j-in-spring-boot-applications-f7e70c6e4cc7)
* [Intro to the Jackson ObjectMapper](https://www.baeldung.com/jackson-object-mapper-tutorial)
* [Jackson Java 8 date / time Not Supported by Default](https://mkyong.com/java/jackson-java-8-date-time-type-java-time-localdate-not-supported-by-default/)
* [Cannot find MockBean in Spring Boot 4.0.0](https://stackoverflow.com/questions/79828472/cannot-find-mockbean-in-spring-boot-4-0-0)
* [Stackoverflow-HowToAssertAnExceptionIsThrown...](https://stackoverflow.com/questions/40268446/how-to-assert-an-exception-is-thrown-with-junit-5)
* [JUnit 6 - Assertions API](https://docs.junit.org/6.0.3/api/org.junit.jupiter.api/org/junit/jupiter/api/Assertions.html)
* [OWASP - Input Validation Cheat Sheet](https://cheatsheetseries.owasp.org/cheatsheets/Input_Validation_Cheat_Sheet.html)
* [GeeksForGeeks - RegEx in Java](https://www.geeksforgeeks.org/java/regular-expressions-in-java/)
* [Baeldung - Regular Expression Java](https://www.baeldung.com/regular-expressions-java)
