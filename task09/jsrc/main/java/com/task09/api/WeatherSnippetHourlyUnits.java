package com.task09.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherSnippetHourlyUnits {
    private String time;
    private String temperature_2m;

    public WeatherSnippetHourlyUnits() {
    }

    public WeatherSnippetHourlyUnits(String temperature_2m, String time) {
        this.temperature_2m = temperature_2m;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(String temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherSnippetHourlyUnits that = (WeatherSnippetHourlyUnits) o;
        return Objects.equals(temperature_2m, that.temperature_2m) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature_2m, time);
    }

    @Override
    public String toString() {
        return "WeatherSnippetHourlyUnits{" +
                "temperature_2m='" + temperature_2m + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
