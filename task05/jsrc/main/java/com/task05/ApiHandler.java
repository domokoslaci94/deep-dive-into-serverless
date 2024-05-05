package com.task05;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.task05.api.ApiRequest;
import com.task05.api.ApiResponse;
import com.task05.dao.EventDao;

import java.util.LinkedHashMap;
import java.util.Map;

@LambdaHandler(lambdaName = "api_handler",
        roleName = "api_handler-role",
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class ApiHandler implements RequestHandler<ApiRequest, Map<String, Object>> {

    private static final String REGION = "eu-central-1";

    private static final EventDao eventDao = new EventDao(configureDynamoDB(), "cmtr-2258fa83-Events");

    private static AmazonDynamoDB configureDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard()
                .withRegion(REGION)
                .build();
    }

    public Map<String, Object> handleRequest(ApiRequest request, Context context) {
        System.out.println("Received request: " + request);

        ApiResponse response = eventDao.save(request);

        Map<String, Object> resultMap = new LinkedHashMap<>();
        resultMap.put("statusCode", 201);
        resultMap.put("event", response);

        return resultMap;
    }


}
