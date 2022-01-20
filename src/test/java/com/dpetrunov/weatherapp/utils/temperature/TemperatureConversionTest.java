package com.dpetrunov.weatherapp.utils.temperature;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemperatureConversionTest {

    @Test
    @DisplayName("Test Convert Kelvin to Celsius")
    public void testConvertCelsiusToKelvin() {

        Kelvin kelvin = new Kelvin(0);
        Temperature celsius = Temperature.convert(kelvin, TemperatureScale.CELSIUS);

        assertEquals(-273.15d, celsius.getRawValue());
    }

    @Test
    @DisplayName("Test Convert Kelvin to Fahrenheit")
    public void testConvertKelvinToFahrenheit() {

        Kelvin kelvin = new Kelvin(0);
        Temperature fahrenheit = Temperature.convert(kelvin, TemperatureScale.FAHRENHEIT);

        assertEquals(-459.66999999999996d, fahrenheit.getRawValue());
    }

    @Test
    @DisplayName("Test Convert Kelvin to Kelvin")
    public void testConvertKelvinToKelvin() {

        Kelvin kelvin = new Kelvin(0);
        Temperature kelvinConverted = Temperature.convert(kelvin, TemperatureScale.KELVIN);

        assertEquals(0.0d, kelvinConverted.getRawValue());
    }
}
