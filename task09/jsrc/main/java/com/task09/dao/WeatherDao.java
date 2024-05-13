package com.task09.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.task09.api.WeatherResponse;
import com.task09.api.WeatherSnippetHourly;
import com.task09.api.WeatherSnippetHourlyUnits;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WeatherDao {
    private final AmazonDynamoDB amazonDynamoDB;
    private final String tableName;

    public WeatherDao(AmazonDynamoDB amazonDynamoDB, String tableName) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.tableName = tableName;
    }

    public void save(WeatherResponse weatherResponse) {
        UUID id = generateId();
        Map<String, AttributeValue> item = convertToItem(id, weatherResponse);

        System.out.println("Saving item: " + item);
        PutItemResult result = amazonDynamoDB.putItem(new PutItemRequest(tableName, item));
        System.out.println("Item saved: " + result);
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }


    private Map<String, AttributeValue> convertToItem(UUID id, WeatherResponse weatherResponse) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", new AttributeValue().withS(id.toString()));
        item.put("forecast", generateForecastFrom(weatherResponse));
        return item;
    }

    private AttributeValue generateForecastFrom(WeatherResponse weatherResponse) {
        AttributeValue body = new AttributeValue();
        body.addMEntry("elevation", new AttributeValue().withN(String.valueOf(weatherResponse.getElevation())));
        body.addMEntry("generationtime_ms", new AttributeValue().withN(String.valueOf(weatherResponse.getGenerationtime_ms())));
        body.addMEntry("hourly", generateHourlyFrom(weatherResponse.getHourly()));
        body.addMEntry("hourly_units", generateHourlyUnitsFrom(weatherResponse.getHourly_units()));
        body.addMEntry("latitude", new AttributeValue().withN(String.valueOf(weatherResponse.getLatitude())));
        body.addMEntry("longitude", new AttributeValue().withN(String.valueOf(weatherResponse.getLongitude())));
        body.addMEntry("timezone", new AttributeValue().withS(weatherResponse.getTimezone()));
        body.addMEntry("timezone_abbreviation", new AttributeValue().withS(weatherResponse.getTimezone_abbreviation()));
        body.addMEntry("utc_offset_seconds", new AttributeValue().withN(String.valueOf(weatherResponse.getUtc_offset_seconds())));
        return body;
    }

    private AttributeValue generateHourlyFrom(WeatherSnippetHourly hourly) {
        AttributeValue body = new AttributeValue();
        body.addMEntry("temperature_2m", new AttributeValue().withS(Arrays.toString(hourly.getTemperature_2m())));
        body.addMEntry("time", new AttributeValue().withS(Arrays.toString(hourly.getTime())));
        return body;
    }

    private AttributeValue generateHourlyUnitsFrom(WeatherSnippetHourlyUnits hourlyUnits) {
        AttributeValue body = new AttributeValue();
        body.addMEntry("temperature_2m", new AttributeValue().withS(hourlyUnits.getTemperature_2m()));
        body.addMEntry("time", new AttributeValue().withS(hourlyUnits.getTime()));
        return body;
    }

}
