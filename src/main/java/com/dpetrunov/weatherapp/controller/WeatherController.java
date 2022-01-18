package com.dpetrunov.weatherapp.controller;

import com.dpetrunov.weatherapp.model.xto.GetWeatherForCityResponseXto;
import com.dpetrunov.weatherapp.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class WeatherController {

	private final WeatherService weatherService;

	public GetWeatherForCityResponseXto getWeatherForCity() {

		return this.weatherService.getWeatherForCity();
	}
}
