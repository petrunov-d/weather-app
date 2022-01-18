package com.dpetrunov.weatherapp.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GetWeatherForCityResponseDto {

	private String cityName;
	private Double lat;
	private Double lng;
	private Double temperatureCelsius;
	private Double temperatureKelvin;
	private Double temperatureFahrenheit;
}