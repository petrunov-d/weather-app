package com.dpetrunov.weatherapp.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author datsko.
 * @version 0.1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "open-weather-map")
public class OpenWeatherMapAPIProperties {

	private String apiKey;
	private String baseUrl;
	private String apiVersion;

	private Integer defaultHttpTimeoutMillis;
}