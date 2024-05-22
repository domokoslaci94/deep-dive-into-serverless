package com.task11.web;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class RequestRouter {

    private static final Map<String, String> CORS_HEADERS = Map.of(
            "Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token",
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Methods", "*",
            "Accept-Version", "*"
    );

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
        response = doHandle(request, internalRequestHandler);

        System.out.println("RequestRouter: Response: " + response);

        addHeaders(response);
        System.out.println("RequestRouter: Response with headers: " + response);
        return response;
    }

    private void addHeaders(APIGatewayProxyResponseEvent response) {
        Map<String, String> originalHeaders = response.getHeaders();
        Map<String, String> newHeaders = new HashMap<>();

        if (originalHeaders != null) {
            newHeaders.putAll(originalHeaders);
        }
        newHeaders.putAll(CORS_HEADERS);
        response.setHeaders(newHeaders);
    }

    private APIGatewayProxyResponseEvent doHandle(APIGatewayProxyRequestEvent request, InternalRequestHandler internalRequestHandler) {
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
