package com.dpetrunov.weatherapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetWeatherForCityResponseDto {

    private String city;
    private String country;

    private Double latitude;
    private Double longitude;

    private Double temperatureCelsius;
    private Double temperatureKelvin;
    private Double temperatureFahrenheit;
}