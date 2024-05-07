package com.task06;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.syndicate.deployment.annotations.events.DynamoDbTriggerEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.task06.dao.EventDao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@LambdaHandler(lambdaName = "audit_producer",
        roleName = "audit_producer-role",
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@DynamoDbTriggerEventSource(targetTable = "Configuration", batchSize = 1)
public class AuditProducer implements RequestHandler<DynamodbEvent, Map<String, Object>> {
    private static final String REGION = "eu-central-1";

    private static final EventDao eventDao = new EventDao(configureDynamoDB(), "cmtr-2258fa83-Audit-test");

    private static AmazonDynamoDB configureDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .build();
    }

    public Map<String, Object> handleRequest(DynamodbEvent event, Context context) {
        System.out.println("Hello from lambda");

        System.out.println("Received event: " + event);
        eventDao.save(event);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("statusCode", 200);
        resultMap.put("body", "Hello from Lambda");
        return resultMap;
    }
}
