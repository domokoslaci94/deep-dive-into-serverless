package com.task11.web;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

public interface InternalRequestHandler {
    APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request);

    String getRequestPath();

    String getRequestMethod();
}
