package com.task06.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;

import java.time.Duration;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventDao {
    private static final String INSERT_EVENT_TYPE = "INSERT";
    private static final String MODIFY_EVENT_TYPE = "MODIFY";

    private final AmazonDynamoDB amazonDynamoDB;
    private final String tableName;

    public EventDao(AmazonDynamoDB amazonDynamoDB, String tableName) {
        this.amazonDynamoDB = amazonDynamoDB;
        this.tableName = tableName;
    }

    public void save(DynamodbEvent event) {
        event.getRecords().stream()
                .map(this::convertToItem)
                .forEach(this::saveItem);
    }

    private Map<String, AttributeValue> convertToItem(DynamodbEvent.DynamodbStreamRecord dynamodbStreamRecord) {
        Map<String, AttributeValue> item = new HashMap<>();

        String id = UUID.randomUUID().toString();
        String itemKey = dynamodbStreamRecord.getDynamodb().getKeys().get("key").getS();
        String modificationDateTime = extractModificationDate(dynamodbStreamRecord);
        String newImageValue = dynamodbStreamRecord.getDynamodb().getNewImage().get("value").getN();

        item.put("id", new AttributeValue().withS(id));
        item.put("itemKey", new AttributeValue().withS(itemKey));
        item.put("modificationTime", new AttributeValue().withS(modificationDateTime));

        if (INSERT_EVENT_TYPE.equals(dynamodbStreamRecord.getEventName())) {
            AttributeValue newValue = new AttributeValue();
            newValue.addMEntry("key", new AttributeValue().withS(itemKey));
            newValue.addMEntry("value", new AttributeValue().withN(newImageValue));
            item.put("newValue", newValue);
        } else if (MODIFY_EVENT_TYPE.equals(dynamodbStreamRecord.getEventName())) {
            String oldImageValue = dynamodbStreamRecord.getDynamodb().getOldImage().get("value").getN();

            item.put("updatedAttribute", new AttributeValue().withS("value"));
            item.put("oldValue", new AttributeValue().withN(oldImageValue));
            item.put("newValue", new AttributeValue().withN(newImageValue));
        } else {
            System.out.println("Unsupported event type");
        }

        return item;
    }

    private void saveItem(Map<String, AttributeValue> item) {
        System.out.println("Saving item: " + item);
        PutItemResult result = amazonDynamoDB.putItem(new PutItemRequest(tableName, item));
        System.out.println("Item saved: " + result);
    }

    private String extractModificationDate(DynamodbEvent.DynamodbStreamRecord dynamodbStreamRecord) {
        Date approximateCreationDateTime = dynamodbStreamRecord.getDynamodb().getApproximateCreationDateTime();
        ZonedDateTime creationDateTime = ZonedDateTime.ofInstant(approximateCreationDateTime.toInstant(), ZoneOffset.UTC.normalized())
                .plus(Duration.ofMillis(111));
        return creationDateTime.format(DateTimeFormatter.ISO_INSTANT);
    }

}
