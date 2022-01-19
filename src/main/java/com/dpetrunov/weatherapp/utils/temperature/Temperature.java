package com.dpetrunov.weatherapp.utils.temperature;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class Temperature {

    private double temperature;

    public abstract double toKelvin();

    public double getRawValue() {

        return this.temperature;
    }

    public static Temperature convert(Temperature from, TemperatureScale to) {

        double kelvin = from.toKelvin();
        return to.fromKelvin(kelvin);
    }
}