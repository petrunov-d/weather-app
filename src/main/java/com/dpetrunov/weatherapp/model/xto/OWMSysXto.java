package com.dpetrunov.weatherapp.model.xto;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class OWMSysXto {

    @Getter(onMethod_ = {@JsonProperty("country")})
    @Setter(onMethod_ = {@JsonProperty("country")})
    private String country;
}