package com.task05.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.task05.api.ApiRequest;
import com.task05.api.ApiResponse;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventDao {
    private final AmazonDynamoDB amazonDynamoDB;
    private final String tableName;

    public EventDao(AmazonDynamoDB amazonDynamoDB, String tableName) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.tableName = tableName;
    }

    public ApiResponse save(ApiRequest request) {
        UUID id = generateId();
        String createdAt = generateTimeStamp();
        Map<String, AttributeValue> item = convertToItem(request, id, createdAt);

        System.out.println("Saving item: " + item);
        PutItemResult result = amazonDynamoDB.putItem(new PutItemRequest(tableName, item));

        System.out.println("Item saved: " + result);
        return generateResponse(request, id, createdAt);
    }

    private String generateTimeStamp() {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);
    }

    private UUID generateId() {
        return UUID.randomUUID();
    }

    private ApiResponse generateResponse(ApiRequest request, UUID id, String createdAt) {
        ApiResponse response = new ApiResponse();
        response.setId(id.toString());
        response.setPrincipalId(request.getPrincipalId());
        response.setCreatedAt(createdAt);
        response.setBody(request.getContent());
        return response;
    }

    private Map<String, AttributeValue> convertToItem(ApiRequest request, UUID id, String createdAt) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", new AttributeValue().withS(id.toString()));
        item.put("principalId", new AttributeValue().withN(String.valueOf(request.getPrincipalId())));
        item.put("createdAt", new AttributeValue().withS(createdAt));
        item.put("body", generateBodyFrom(request.getContent()));
        return item;
    }

    private AttributeValue generateBodyFrom(Map<String, String> eventBody) {
        AttributeValue body = new AttributeValue();
        eventBody.forEach((key, value) -> body.addMEntry(key, new AttributeValue().withS(value)));
        return body;
    }
}
