package com.dpetrunov.weatherapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private static final String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/webjars/swagger-ui/**",
            "/webjars/swagger-ui.html",
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        // @formatter:off
        http
            .cors()
            .and()
            .authorizeExchange()
                .pathMatchers(SWAGGER_WHITELIST)
                .permitAll()
            .and()
            .authorizeExchange()
                .anyExchange()
                .authenticated()
            .and()
            .httpBasic();

        // @formatter:off
        return http.build();
    }
}
