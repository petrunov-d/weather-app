package com.dpetrunov.weatherapp.config;

import com.dpetrunov.weatherapp.model.properties.OpenWeatherMapAPIProperties;
import com.dpetrunov.weatherapp.utils.WebClientUtil;
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
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AppConfiguration {

    public static final String QUERY_PARAM_API_KEY = "appid";

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
                .filter(authenticatingFilter())
                .filter(logFilter())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    private ExchangeFilterFunction logFilter() {

        return (clientRequest, next) -> {

            log.info("Request to {}, method: {}, headers: {} ", clientRequest.url(), clientRequest.method(), clientRequest.headers());
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction authenticatingFilter() {

        return new ExchangeFilterFunction() {

            @Override
            @SneakyThrows
            public Mono<ClientResponse> filter(ClientRequest clientRequest, ExchangeFunction next) {

                // request already had authentication info so we just forward.
                if (WebClientUtil.authQueryParamExists(clientRequest.url().getQuery())) {

                    return next.exchange(clientRequest);
                }

                //otherwise append authentication query to each request
                URI oldUri = new URI(clientRequest.url().toString());

                String queryStringWithAuthentication = WebClientUtil
                        .queryStringWithAuthentication(oldUri.getQuery(), openWeatherMapAPIProperties.getApiKey());

                URI authenticatedUri = new URI(oldUri.getScheme(), oldUri.getAuthority(),
                        oldUri.getPath(), queryStringWithAuthentication, oldUri.getFragment());

                ClientRequest filteredRequest = ClientRequest
                        .from(clientRequest)
                        .url(authenticatedUri)
                        .build();

                return next.exchange(filteredRequest);
            }
        };
    }
}