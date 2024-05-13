package com.task09.dao.entity;


import com.task09.api.WeatherResponse;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.Objects;

@DynamoDbBean
public class WeatherEntity {

    private String id;
    private WeatherResponse forecast;

    public WeatherEntity() {
    }

    public WeatherEntity(String id, WeatherResponse forecast) {
        this.id = id;
        this.forecast = forecast;
    }

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public WeatherResponse getForecast() {
        return forecast;
    }

    public void setForecast(WeatherResponse forecast) {
        this.forecast = forecast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeatherEntity that = (WeatherEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(forecast, that.forecast);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, forecast);
    }

    @Override
    public String toString() {
        return "WeatherEntity{" +
                "id='" + id + '\'' +
                ", forecast=" + forecast +
                '}';
    }
}
