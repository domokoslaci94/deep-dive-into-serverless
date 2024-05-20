package com.task10.web.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperDecorator {

    private final ObjectMapper decorated;

    public ObjectMapperDecorator(ObjectMapper decorated) {
        this.decorated = decorated;
    }

    public String writeValueAsString(Object value) {
        String result;
        try {
            result = decorated.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public <T> T readValue(String content, Class<T> valueType) {
        T result;
        try {
            result = decorated.readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
