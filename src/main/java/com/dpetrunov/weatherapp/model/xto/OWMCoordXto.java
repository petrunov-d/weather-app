package com.dpetrunov.weatherapp.model.xto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OWMCoordXto {

    @Getter(onMethod_ = {@JsonProperty("lon")})
    @Setter(onMethod_ = {@JsonProperty("lon")})
    private double lon;

    @Getter(onMethod_ = {@JsonProperty("lat")})
    @Setter(onMethod_ = {@JsonProperty("lat")})
    private double lat;
}