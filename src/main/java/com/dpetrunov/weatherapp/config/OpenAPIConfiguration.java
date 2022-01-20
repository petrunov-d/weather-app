package com.dpetrunov.weatherapp.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {


    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(getInfo());
    }

    private Info getInfo() {

        return new Info()
                .title("Real Time Weather Data App")
                .description("Gets you real time weather data")
                .license(getLicense());
    }

    private License getLicense() {
        return new License()
                .name("Unlicense")
                .url("https://unlicense.org/");
    }
}