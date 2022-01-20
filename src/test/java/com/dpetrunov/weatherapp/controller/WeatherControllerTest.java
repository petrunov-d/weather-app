package com.dpetrunov.weatherapp.controller;

import com.dpetrunov.weatherapp.model.dto.GetWeatherForCityResponseDto;
import com.dpetrunov.weatherapp.service.WeatherService;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.HttpClientErrorException;
import reactor.core.publisher.Flux;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = WeatherController.class)
public class WeatherControllerTest {

    @MockBean
    WeatherService weatherService;

    @Autowired
    private WebTestClient webClient;

    @Test
    @DisplayName("Test Get Weather Data By City")
    void testGetWeatherDataForCity() {

        var countryCode = CountryCode.UK;
        var city = "London";

        var sampleResponse = getSampleResponse();

        Mockito.when(this.weatherService.getWeatherForCity(countryCode, city)).thenReturn(sampleResponse);

        this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather/realtime")
                        .queryParam(WeatherController.QUERY_PARAM_COUNTRY_CODE, CountryCode.UK)
                        .queryParam(WeatherController.QUERY_PARAM_CITY, city)
                        .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString(("admin" + ":" + "admin").getBytes(UTF_8)))
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(GetWeatherForCityResponseDto.class);

        verify(this.weatherService).getWeatherForCity(countryCode, city);
    }

    @Test
    @DisplayName("Test Error Handling")
    void testErrorHandling() {

        var countryCode = CountryCode.UK;
        var city = "London";

        Mockito.when(this.weatherService.getWeatherForCity(countryCode, city)).thenThrow(
                new HttpClientErrorException(HttpStatus.NOT_FOUND, "city not found"));

        this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/weather/realtime")
                        .queryParam(WeatherController.QUERY_PARAM_COUNTRY_CODE, CountryCode.UK)
                        .queryParam(WeatherController.QUERY_PARAM_CITY, city)
                        .build())
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString(("admin" + ":" + "admin").getBytes(UTF_8)))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class);

        verify(this.weatherService).getWeatherForCity(countryCode, city);
    }

    private Flux<GetWeatherForCityResponseDto> getSampleResponse() {

        GetWeatherForCityResponseDto getWeatherForCityResponseDto = GetWeatherForCityResponseDto
                .builder()
                .country("uk")
                .city("london")
                .temperatureFahrenheit(0.0)
                .temperatureKelvin(0.0)
                .temperatureCelsius(0.0)
                .latitude(0.0)
                .longitude(0.0)
                .build();

        return Flux.just(getWeatherForCityResponseDto);
    }
}
