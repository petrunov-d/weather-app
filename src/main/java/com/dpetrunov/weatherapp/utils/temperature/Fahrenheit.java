package com.dpetrunov.weatherapp.utils.temperature;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class Fahrenheit extends Temperature {

    public static final Double FAHRENHEIT_OFFSET = 32.00D;

    public Fahrenheit(double temperatureFahrenheit) {

        super(temperatureFahrenheit);
    }

    @Override
    public double toKelvin() {

        return 273.5f + ((this.getTemperature() - FAHRENHEIT_OFFSET) * (5.0f / 9.0f));
    }
}
