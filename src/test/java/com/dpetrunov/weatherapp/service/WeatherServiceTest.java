package com.dpetrunov.weatherapp.service;

import com.dpetrunov.weatherapp.model.xto.GetWeatherForCityResponseXto;
import com.dpetrunov.weatherapp.model.xto.OWMCoordXto;
import com.dpetrunov.weatherapp.model.xto.OWMMainXto;
import com.dpetrunov.weatherapp.model.xto.OWMSysXto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    WebClient webClient;

    WireMockServer wireMockServer;

    WeatherService weatherService;

    @BeforeEach
    void setUp() throws Exception {

        this.wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        this.wireMockServer.start();

        this.wireMockServer.stubFor(get(urlPathMatching("/weather"))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
                        .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(OBJECT_MAPPER.writeValueAsString(getSampleResponse()))));

        this.webClient = WebClient.builder().baseUrl(wireMockServer.baseUrl()).build();

        this.weatherService = new WeatherService(webClient);
    }

    @Test
    @DisplayName("Test Get Weather Data By Country Name")
    void testGetWeatherDataByCityAndCountryCode() throws JsonProcessingException {

        var getWeatherForCityResponseDtoFlux = this.weatherService.getWeatherForCity(CountryCode.UK, "Test");
        var list = getWeatherForCityResponseDtoFlux.toStream().toList();

        var getWeatherForCityResponseDto = list.get(0);
        var getWeatherForCityResponseXto = getSampleResponse();

        assertEquals(getWeatherForCityResponseDto.getCountry(), getWeatherForCityResponseXto.getSys().getCountry());
        assertEquals(getWeatherForCityResponseDto.getTemperatureKelvin(), getWeatherForCityResponseXto.getMain().getTemp());
        assertEquals(getWeatherForCityResponseDto.getLatitude(), getWeatherForCityResponseXto.getCoordinates().getLat());
        assertEquals(getWeatherForCityResponseDto.getLongitude(), getWeatherForCityResponseXto.getCoordinates().getLon());
        assertEquals(getWeatherForCityResponseDto.getCity(), getWeatherForCityResponseXto.getName());
    }

    @AfterEach
    void tearDown() {
        this.wireMockServer.stop();
    }

    private GetWeatherForCityResponseXto getSampleResponse() {

        GetWeatherForCityResponseXto getWeatherForCityResponseXto = new GetWeatherForCityResponseXto();

        getWeatherForCityResponseXto.setName("test");
        getWeatherForCityResponseXto.setTimezone(0);

        OWMMainXto owmMainXto = new OWMMainXto();
        owmMainXto.setTemp(0.0);

        OWMCoordXto owmCoordXto = new OWMCoordXto();
        owmCoordXto.setLat(0.0);
        owmCoordXto.setLon(0.0);

        OWMSysXto owmSysXto = new OWMSysXto();
        owmSysXto.setCountry(CountryCode.UK.name());

        getWeatherForCityResponseXto.setMain(owmMainXto);
        getWeatherForCityResponseXto.setCoordinates(owmCoordXto);
        getWeatherForCityResponseXto.setSys(owmSysXto);

        return getWeatherForCityResponseXto;
    }
}
