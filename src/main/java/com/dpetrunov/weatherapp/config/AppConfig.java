package com.dpetrunov.weatherapp.config;

import com.dpetrunov.weatherapp.config.properties.OpenWeatherMapAPIProperties;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

	private static final String URL_PARAM_API_KEY_NAME = "appid";

	private final OpenWeatherMapAPIProperties openWeatherMapAPIProperties;

	@Bean
	@Primary
	public ObjectMapper objectMapper() {

		var defaultObjectMapper = new ObjectMapper()
				.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)
				.enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
				.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING)
				.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
				.disable(SerializationFeature.WRAP_ROOT_VALUE)
				.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)
				.enable(JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION)
				.findAndRegisterModules();

		defaultObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		defaultObjectMapper.disable(MapperFeature.USE_GETTERS_AS_SETTERS);
		defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		defaultObjectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		return defaultObjectMapper;
	}

	@Bean
	public WebClient openWeatherMapWebClient() {

		var httpClient = HttpClient.create()
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.openWeatherMapAPIProperties.getDefaultHttpTimeoutMillis())
				.responseTimeout(Duration.ofMillis(this.openWeatherMapAPIProperties.getDefaultHttpTimeoutMillis()))
				.doOnConnected(conn ->
						conn.addHandlerLast(new ReadTimeoutHandler(this.openWeatherMapAPIProperties.getDefaultHttpTimeoutMillis(),
										TimeUnit.MILLISECONDS))
								.addHandlerLast(new WriteTimeoutHandler(this.openWeatherMapAPIProperties.getDefaultHttpTimeoutMillis(),
										TimeUnit.MILLISECONDS)));

		return WebClient.builder()
				.baseUrl(this.openWeatherMapAPIProperties.getBaseUrl())
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultUriVariables(Collections.singletonMap(URL_PARAM_API_KEY_NAME, this.openWeatherMapAPIProperties.getApiKey()))
				.clientConnector(new ReactorClientHttpConnector(httpClient))
				.build();
	}
}
