package com.task10.web.table.handler;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task10.dao.DynamoDBTableDecorator;
import com.task10.dao.entity.TableEntity;
import com.task10.web.InternalRequestHandler;
import com.task10.web.util.ObjectMapperDecorator;

public class SingleTableInternalRequestHandler implements InternalRequestHandler {

    private static final String REQUEST_METHOD = "GET";
    private static final String REQUEST_PATH = "^/tables/(?<tableId>\\d+)$";
    private final DynamoDBTableDecorator<TableEntity> table;
    private final ObjectMapperDecorator objectMapper;

    public SingleTableInternalRequestHandler(DynamoDBTableDecorator<TableEntity> table, ObjectMapperDecorator objectMapper) {
        this.table = table;
        this.objectMapper = objectMapper;
    }

    private int extractIdFrom(APIGatewayProxyRequestEvent request) {
        return Integer.parseInt(request.getPathParameters().get("tableId"));
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.println("SingleTableRequestHandler: Received request: " + request);
        TableEntity tableEntity = new TableEntity();
        tableEntity.setId(extractIdFrom(request));
        TableEntity item = table.getItem(tableEntity);

        return new APIGatewayProxyResponseEvent()
                .withBody(objectMapper.writeValueAsString(item))
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
