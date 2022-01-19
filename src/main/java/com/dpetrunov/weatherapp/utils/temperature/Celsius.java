package com.dpetrunov.weatherapp.utils.temperature;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Celsius extends Temperature {

    public Celsius(double temperatureCelsius) {
        super(temperatureCelsius);
    }

    @Override
    public double toKelvin() {

        return this.getTemperature() + Kelvin.ABSOLUTE_ZERO;
    }
}