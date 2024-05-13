package com.task09.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task09.api.WeatherResponse;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class WeatherService {

    private static final String OPEN_METEO_API = "https://api.open-meteo.com/v1/forecast" +
            "?latitude=52.52&longitude=13.41" +
            "&current=temperature_2m,wind_speed_10m" +
            "&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WeatherService(HttpClient httpClient, ObjectMapper objectMapper) {
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
    }

    public WeatherResponse queryWeather() {
        HttpResponse<String> response;

        response = sendRequest();
        System.out.println("Received response: " + response);

        WeatherResponse weatherResponse = convertResponse(response);
        System.out.println("Converted response to: " + weatherResponse);

        return weatherResponse;
    }

    private WeatherResponse convertResponse(HttpResponse<String> response) {
        WeatherResponse weatherResponse;
        try {
            weatherResponse = objectMapper.readValue(response.body(), WeatherResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return weatherResponse;
    }

    private HttpResponse<String> sendRequest() {
        HttpResponse<String> response;
        try {
            response = httpClient.send(generateRequest(), HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }


    private HttpRequest generateRequest() {
        URI targetUri;
        try {
            targetUri = new URI(OPEN_METEO_API);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return HttpRequest.newBuilder()
                .GET()
                .uri(targetUri)
                .build();
    }
}
