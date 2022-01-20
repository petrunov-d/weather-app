package com.dpetrunov.weatherapp.controller;

import com.dpetrunov.weatherapp.model.dto.GetWeatherForCityResponseDto;
import com.dpetrunov.weatherapp.service.WeatherService;
import com.neovisionaries.i18n.CountryCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Real Time Weather Data")
public class WeatherController {

    private final WeatherService weatherService;

    public static final String QUERY_PARAM_COUNTRY_CODE = "countryCode";
    public static final String QUERY_PARAM_CITY = "city";

    @GetMapping(path = "/weather/realtime")
    @Operation(description = "Gets The Weather Data For A City. If abigious returns more than one result.", parameters = {
            @Parameter(name = "countryCode", in = ParameterIn.QUERY, description = "Country Code, compliant with ISO 3166"),
            @Parameter(name = "city", in = ParameterIn.QUERY, required = true, description = "The City To Return Data for."),
    })
    public Flux<GetWeatherForCityResponseDto> getWeatherForCity(@RequestParam(name = QUERY_PARAM_COUNTRY_CODE, required = false) CountryCode countryCode,
                                                                @RequestParam(name = QUERY_PARAM_CITY) String city) {

        return this.weatherService.getWeatherForCity(countryCode, city);
    }
}