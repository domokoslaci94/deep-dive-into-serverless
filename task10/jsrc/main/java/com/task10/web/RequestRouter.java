package com.task10.web;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestRouter {

    private final Set<InternalRequestHandler> internalRequestHandlers;

    public RequestRouter(Set<InternalRequestHandler> internalRequestHandlers) {
        this.internalRequestHandlers = internalRequestHandlers;
    }

    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.println("RequestRouter: Routing request: " + request);
        System.out.println("RequestRouter: Available handlers: " + internalRequestHandlers);

        InternalRequestHandler internalRequestHandler = findHandler(request);
        System.out.println("RequestRouter: Found suitable handler: " + internalRequestHandler);
        APIGatewayProxyResponseEvent response;
        try {
            response = internalRequestHandler.handle(request);
        } catch (Exception e) {
            System.out.println("RequestRouter: Error handling request: " + e);
            response = new APIGatewayProxyResponseEvent()
                    .withStatusCode(400)
                    .withBody(e.getMessage());
        }
        return response;
    }

    private InternalRequestHandler findHandler(APIGatewayProxyRequestEvent request) {
        List<InternalRequestHandler> handlerList = internalRequestHandlers.stream()
                .filter(requestHandler -> request.getPath().matches(requestHandler.getRequestPath()))
                .filter(requestHandler -> request.getHttpMethod().equals(requestHandler.getRequestMethod()))
                .collect(Collectors.toList());

        if (handlerList.isEmpty()) {
            System.out.println("RequestRouter: No handler found for request");
            throw new IllegalArgumentException("No handler found for request");
        }

        if (handlerList.size() > 1) {
            System.out.println("RequestRouter: Ambiguous mapping, found multiple handlers: " + handlerList);
            throw new IllegalArgumentException("Ambiguous mapping, found multiple handlers: " + handlerList);
        }

        return handlerList.get(0);
    }

}
