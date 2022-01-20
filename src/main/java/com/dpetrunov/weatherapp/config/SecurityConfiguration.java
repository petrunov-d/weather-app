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

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {

        // @formatter:off
        http
            .cors()
            .and()
            .authorizeExchange((exchanges) -> exchanges
                    .pathMatchers("/actuator/health")
                        .permitAll()
                    .anyExchange()
                        .authenticated()
                    .and()
                    .httpBasic());

        // @formatter:off
        return http.build();
    }
}