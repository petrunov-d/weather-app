package com.dpetrunov.weatherapp.service;

import com.dpetrunov.weatherapp.model.dto.ExceptionContextDto;
import com.dpetrunov.weatherapp.model.dto.GetWeatherForCityResponseDto;
import com.dpetrunov.weatherapp.model.xto.GetWeatherForCityResponseXto;
import com.dpetrunov.weatherapp.model.xto.OWMMainXto;
import com.dpetrunov.weatherapp.utils.temperature.Kelvin;
import com.dpetrunov.weatherapp.utils.temperature.Temperature;
import com.dpetrunov.weatherapp.utils.temperature.TemperatureScale;
import com.neovisionaries.i18n.CountryCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private static final String QUERY_PARAM_SEARCH_CRITERIA = "q";
    private static final String PATH_WEATHER = "/weather";

    private final WebClient webClient;

    public Flux<GetWeatherForCityResponseDto> getWeatherForCity(CountryCode countryCode, String city) {

        var queryString = city + (countryCode == null ? "" : "," + countryCode);

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_WEATHER)
                        .queryParam(QUERY_PARAM_SEARCH_CRITERIA, queryString)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> response.bodyToMono(ExceptionContextDto.class)
                        .flatMap(error -> Mono.error(new HttpClientErrorException(response.statusCode(), error.getMessage()))))
                .onStatus(HttpStatus::is5xxServerError, response -> response.bodyToMono(ExceptionContextDto.class)
                        .flatMap(error -> Mono.error(new HttpServerErrorException(response.statusCode(), error.getMessage()))))
                .bodyToFlux(GetWeatherForCityResponseXto.class)
                .map(x -> {

                    OWMMainXto owmMainXto = x.getMain();

                    Temperature temperatureKelvin = null;
                    Temperature temperatureCelsius = null;
                    Temperature temperatureFahrenheit = null;

                    if (owmMainXto != null) {

                        temperatureKelvin = new Kelvin(owmMainXto.getTemp());
                        temperatureCelsius = Temperature.convert(temperatureKelvin, TemperatureScale.CELSIUS);
                        temperatureFahrenheit = Temperature.convert(temperatureKelvin, TemperatureScale.FAHRENHEIT);
                    }

                    return GetWeatherForCityResponseDto
                            .builder()
                            .city(x.getName())
                            .country(x.getSys() == null ? null : x.getSys().getCountry())
                            .latitude(x.getCoordinates() == null ? null : x.getCoordinates().getLat())
                            .longitude(x.getCoordinates() == null ? null : x.getCoordinates().getLon())
                            .temperatureKelvin(temperatureKelvin == null ? null : temperatureKelvin.getRawValue())
                            .temperatureCelsius(temperatureCelsius == null ? null : temperatureCelsius.getRawValue())
                            .temperatureFahrenheit(temperatureFahrenheit == null ? null : temperatureFahrenheit.getRawValue())
                            .build();
                });
    }
}