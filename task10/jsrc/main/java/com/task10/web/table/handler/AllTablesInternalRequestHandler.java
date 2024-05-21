package com.task10.web.table.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task10.dao.DynamoDBTableDecorator;
import com.task10.dao.entity.TableEntity;
import com.task10.web.InternalRequestHandler;
import com.task10.web.table.api.AllTablesResponse;
import com.task10.web.util.ObjectMapperDecorator;

import java.util.List;

public class AllTablesInternalRequestHandler implements InternalRequestHandler {

    private static final String REQUEST_METHOD = "GET";
    private static final String REQUEST_PATH = "^/tables$";
    private final DynamoDBTableDecorator<TableEntity> table;
    private final ObjectMapperDecorator objectMapper;

    public AllTablesInternalRequestHandler(DynamoDBTableDecorator<TableEntity> table, ObjectMapperDecorator objectMapper) {
        this.table = table;
        this.objectMapper = objectMapper;
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.println("AllTablesRequestHandler: Received request: " + request);

        List<TableEntity> tables = table.getAllItem();

        System.out.println("AllTablesRequestHandler: Returned list of tables:" + tables);

        return new APIGatewayProxyResponseEvent()
                .withBody(objectMapper.writeValueAsString(new AllTablesResponse(tables)))
                .withStatusCode(200);
    }

    @Override
    public String getRequestPath() {
        return REQUEST_PATH;
    }

    @Override
    public String getRequestMethod() {
        return REQUEST_METHOD;
    }

}
