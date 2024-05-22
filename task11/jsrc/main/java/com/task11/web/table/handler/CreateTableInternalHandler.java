package com.task11.web.table.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task11.dao.DynamoDBTableDecorator;
import com.task11.dao.entity.TableEntity;
import com.task11.web.InternalRequestHandler;
import com.task11.web.table.api.CreateTableResponse;
import com.task11.web.util.ObjectMapperDecorator;

public class CreateTableInternalHandler implements InternalRequestHandler {

    private static final String REQUEST_METHOD = "POST";
    private static final String REQUEST_PATH = "^/tables$";
    private final DynamoDBTableDecorator<TableEntity> table;
    private final ObjectMapperDecorator objectMapper;

    public CreateTableInternalHandler(DynamoDBTableDecorator<TableEntity> table, ObjectMapperDecorator objectMapper) {
        this.table = table;
        this.objectMapper = objectMapper;
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.println("CreateTableInternalHandler: Received request: " + request);
        String requestString = request.getBody();

        TableEntity tableEntity = objectMapper.readValue(requestString, TableEntity.class);
        table.putItem(tableEntity);
        CreateTableResponse responseBody = new CreateTableResponse(tableEntity.getId());

        return new APIGatewayProxyResponseEvent().withBody(objectMapper.writeValueAsString(responseBody))
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
