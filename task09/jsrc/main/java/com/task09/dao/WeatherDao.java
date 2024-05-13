package com.task09.dao;

import com.task09.api.WeatherResponse;
import com.task09.dao.entity.WeatherEntity;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;

import java.util.UUID;

public class WeatherDao {

    private final DynamoDbTable<WeatherEntity> table;

    public WeatherDao(DynamoDbTable<WeatherEntity> table) {
        this.table = table;
    }

    public void save(WeatherResponse weatherResponse) {
        WeatherEntity weatherEntity = new WeatherEntity();
        weatherEntity.setId(UUID.randomUUID().toString());
        weatherEntity.setForecast(weatherResponse);
        table.putItem(weatherEntity);
    }

}
