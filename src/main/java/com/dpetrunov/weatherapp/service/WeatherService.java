package com.dpetrunov.weatherapp.service;

import com.dpetrunov.weatherapp.model.xto.GetWeatherForCityResponseXto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WeatherService {

	private final WebClient webClient;

	@Scheduled(fixedRate = 1000)
	public GetWeatherForCityResponseXto getWeatherForCity() {

		String result = this.webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/weather")
						.queryParam("q", "Dubai")
						.queryParam("appid", "24e3fa860c58db24391dfa00fde3ce63")
						.build())
				.retrieve()
				.bodyToMono(String.class)
				.block();

		return null;
	}
}
