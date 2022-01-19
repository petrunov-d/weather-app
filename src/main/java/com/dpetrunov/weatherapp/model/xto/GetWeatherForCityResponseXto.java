package com.dpetrunov.weatherapp.model.xto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class GetWeatherForCityResponseXto {
    
    @Getter(onMethod_ = {@JsonProperty("coord")})
    @Setter(onMethod_ = {@JsonProperty("coord")})
    private OWMCoordXto coordinates;

    @Getter(onMethod_ = {@JsonProperty("main")})
    @Setter(onMethod_ = {@JsonProperty("main")})
    private OWMMainXto main;

    @Getter(onMethod_ = {@JsonProperty("sys")})
    @Setter(onMethod_ = {@JsonProperty("sys")})
    private OWMSysXto sys;

    @Getter(onMethod_ = {@JsonProperty("timezone")})
    @Setter(onMethod_ = {@JsonProperty("timezone")})
    private long timezone;

    @Getter(onMethod_ = {@JsonProperty("name")})
    @Setter(onMethod_ = {@JsonProperty("name")})
    private String name;
}