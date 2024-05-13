package com.task09.api;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@DynamoDbBean
public class WeatherResponse {
    private double elevation;
    private double generationtime_ms;
    private WeatherSnippetHourly hourly;
    private WeatherSnippetHourlyUnits hourly_units;
    private double latitude;
    private double longitude;
    private String timezone;
    private String timezone_abbreviation;
    private double utc_offset_seconds;

    public WeatherResponse() {
    }

    public WeatherResponse(double elevation, double generationtime_ms, WeatherSnippetHourly hourly, WeatherSnippetHourlyUnits hourly_units, double latitude, double longitude, String timezone, String timezone_abbreviation, double utc_offset_seconds) {
        this.elevation = elevation;
        this.generationtime_ms = generationtime_ms;
        this.hourly = hourly;
        this.hourly_units = hourly_units;
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = timezone;
        this.timezone_abbreviation = timezone_abbreviation;
        this.utc_offset_seconds = utc_offset_seconds;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getGenerationtime_ms() {
        return generationtime_ms;
    }

    public void setGenerationtime_ms(double generationtime_ms) {
        this.generationtime_ms = generationtime_ms;
    }

    public WeatherSnippetHourly getHourly() {
        return hourly;
    }

    public void setHourly(WeatherSnippetHourly hourly) {
        this.hourly = hourly;
    }

    public WeatherSnippetHourlyUnits getHourly_units() {
        return hourly_units;
    }

    public void setHourly_units(WeatherSnippetHourlyUnits hourly_units) {
        this.hourly_units = hourly_units;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getTimezone_abbreviation() {
        return timezone_abbreviation;
    }

    public void setTimezone_abbreviation(String timezone_abbreviation) {
        this.timezone_abbreviation = timezone_abbreviation;
    }

    public double getUtc_offset_seconds() {
        return utc_offset_seconds;
    }

    public void setUtc_offset_seconds(double utc_offset_seconds) {
        this.utc_offset_seconds = utc_offset_seconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherResponse that = (WeatherResponse) o;
        return Double.compare(elevation, that.elevation) == 0 && Double.compare(generationtime_ms, that.generationtime_ms) == 0 && Double.compare(latitude, that.latitude) == 0 && Double.compare(longitude, that.longitude) == 0 && Double.compare(utc_offset_seconds, that.utc_offset_seconds) == 0 && Objects.equals(hourly, that.hourly) && Objects.equals(hourly_units, that.hourly_units) && Objects.equals(timezone, that.timezone) && Objects.equals(timezone_abbreviation, that.timezone_abbreviation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elevation, generationtime_ms, hourly, hourly_units, latitude, longitude, timezone, timezone_abbreviation, utc_offset_seconds);
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "elevation=" + elevation +
                ", generationtime_ms=" + generationtime_ms +
                ", hourly=" + hourly +
                ", hourly_units=" + hourly_units +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezon='" + timezone + '\'' +
                ", timezone_abbreviation='" + timezone_abbreviation + '\'' +
                ", utc_offset_seconds=" + utc_offset_seconds +
                '}';
    }
}
