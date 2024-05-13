package com.task09.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherSnippetHourly {
    private double[] temperature_2m;
    private String[] time;

    public WeatherSnippetHourly() {
    }

    public WeatherSnippetHourly(double[] temperature_2m, String[] time) {
        this.temperature_2m = temperature_2m;
        this.time = time;
    }

    public double[] getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(double[] temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public String[] getTime() {
        return time;
    }

    public void setTime(String[] time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherSnippetHourly that = (WeatherSnippetHourly) o;
        return Objects.deepEquals(temperature_2m, that.temperature_2m) && Objects.deepEquals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(temperature_2m), Arrays.hashCode(time));
    }

    @Override
    public String toString() {
        return "WeatherSnippetHourly{" +
                "temperature_2m=" + Arrays.toString(temperature_2m) +
                ", time=" + Arrays.toString(time) +
                '}';
    }
}
