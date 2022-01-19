package com.dpetrunov.weatherapp.utils.temperature;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Kelvin extends Temperature {

    public static final Double ABSOLUTE_ZERO = 273.15D;

    public Kelvin(double temperatureKelvin) {

        super(temperatureKelvin);
    }

    @Override
    public double toKelvin() {

        return this.getTemperature();
    }
}
