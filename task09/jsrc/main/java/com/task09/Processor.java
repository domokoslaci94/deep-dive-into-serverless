package com.task09;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.TracingMode;
import com.task09.dao.WeatherDao;
import com.task09.dao.entity.WeatherEntity;
import com.task09.service.WeatherService;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

@LambdaHandler(lambdaName = "processor",
        roleName = "processor-role",
        tracingMode = TracingMode.Active,
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@EnvironmentVariables(value = {
        @EnvironmentVariable(key = "region", value = "${region}"),
        @EnvironmentVariable(key = "target_table", value = "${target_table}")
})
@LambdaUrlConfig
public class Processor implements RequestHandler<Object, Map<String, Object>> {

    private static final String REGION = System.getenv("region");
    private static final String TABLE_NAME = System.getenv("target_table");

    private static final WeatherService WEATHER_SERVICE = new WeatherService(HttpClient.newHttpClient(), new ObjectMapper());
    private static final DynamoDbTable<WeatherEntity> WEATHER_TABLE = configureDynamoDBTable();
    private static final WeatherDao WEATHER_DAO = new WeatherDao(WEATHER_TABLE);

    private static DynamoDbTable<WeatherEntity> configureDynamoDBTable() {
        return DynamoDbEnhancedClient.create()
                .table(TABLE_NAME, TableSchema.fromBean(WeatherEntity.class));
    }

    public Map<String, Object> handleRequest(Object request, Context context) {
        System.out.println("Hello from lambda");

        WEATHER_DAO.save(WEATHER_SERVICE.queryWeather());

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("statusCode", 200);
        resultMap.put("body", "Hello from Lambda");
        return resultMap;
    }
}
