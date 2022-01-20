package com.dpetrunov.weatherapp.utils.temperature;

public enum TemperatureScale {

    KELVIN,
    CELSIUS,
    FAHRENHEIT;

    public Temperature fromKelvin(double temperatureInKelvin) {

        switch (this) {

            case KELVIN -> {

                return new Kelvin(temperatureInKelvin);
            }
            case CELSIUS -> {

                return new Celsius(temperatureInKelvin - Kelvin.ABSOLUTE_ZERO);

            }
            case FAHRENHEIT -> {

                return new Fahrenheit(((temperatureInKelvin - Kelvin.ABSOLUTE_ZERO) * 1.8) + Fahrenheit.FAHRENHEIT_OFFSET);
            }
        }

        throw new IllegalArgumentException("No conversion implementation for scale: " + this.name());
    }
}