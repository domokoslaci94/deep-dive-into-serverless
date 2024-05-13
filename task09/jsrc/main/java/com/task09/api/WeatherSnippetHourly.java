package com.task09.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDbBean
public class WeatherSnippetHourly {
    private List<Double> temperature_2m;
    private List<String> time;

    public WeatherSnippetHourly() {
    }

    public WeatherSnippetHourly(List<Double> temperature_2m, List<String> time) {
        this.temperature_2m = temperature_2m;
        this.time = time;
    }

    public List<Double> getTemperature_2m() {
        return temperature_2m;
    }

    public void setTemperature_2m(List<Double> temperature_2m) {
        this.temperature_2m = temperature_2m;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherSnippetHourly that = (WeatherSnippetHourly) o;
        return Objects.equals(temperature_2m, that.temperature_2m) && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(temperature_2m, time);
    }

    @Override
    public String toString() {
        return "WeatherSnippetHourly{" +
                "temperature_2m=" + temperature_2m +
                ", time=" + time +
                '}';
    }
}
