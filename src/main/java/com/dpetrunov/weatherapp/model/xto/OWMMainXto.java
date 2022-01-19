package com.dpetrunov.weatherapp.model.xto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OWMMainXto {

    @Getter(onMethod_ = {@JsonProperty("temp")})
    @Setter(onMethod_ = {@JsonProperty("temp")})
    private double temp;
}