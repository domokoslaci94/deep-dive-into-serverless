package com.task11.web.reservation;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task11.dao.DynamoDBTableDecorator;
import com.task11.dao.entity.ReservationEntity;
import com.task11.web.InternalRequestHandler;
import com.task11.web.reservation.api.AllReservationsResponse;
import com.task11.web.util.ObjectMapperDecorator;

import java.util.List;

public class GetReservationInternalRequestHandler implements InternalRequestHandler {

    private static final String REQUEST_METHOD = "GET";
    private static final String REQUEST_PATH = "^/reservations$";
    private final DynamoDBTableDecorator<ReservationEntity> table;
    private final ObjectMapperDecorator objectMapper;

    public GetReservationInternalRequestHandler(DynamoDBTableDecorator<ReservationEntity> table, ObjectMapperDecorator objectMapper) {
        this.table = table;
        this.objectMapper = objectMapper;
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.println("GetReservationRequestHandler: Received request: " + request);
        List<ReservationEntity> reservations = table.getAllItem();
        return new APIGatewayProxyResponseEvent()
                .withBody(objectMapper.writeValueAsString(new AllReservationsResponse(reservations)))
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
