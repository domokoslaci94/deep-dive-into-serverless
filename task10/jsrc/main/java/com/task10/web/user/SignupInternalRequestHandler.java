package com.task10.web.user;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task10.config.ApplicationContext;
import com.task10.web.InternalRequestHandler;
import com.task10.web.user.api.SignupRequest;
import com.task10.web.util.ObjectMapperDecorator;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminCreateUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.MessageActionType;

public class SignupInternalRequestHandler implements InternalRequestHandler {

    private static final String REQUEST_METHOD = "POST";
    private static final String REQUEST_PATH = "^/signup$";

    private final ObjectMapperDecorator objectMapper;
    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;

    public SignupInternalRequestHandler(ObjectMapperDecorator objectMapper, CognitoIdentityProviderClient cognitoClient, String userPoolId, String clientId) {
        this.objectMapper = objectMapper;
        this.cognitoClient = cognitoClient;
        this.userPoolId = userPoolId;
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.println("SignupRequestHandler: Received request: " + request);
        SignupRequest signupRequest = objectMapper.readValue(request.getBody(), SignupRequest.class);

        AdminCreateUserResponse adminCreateUserResponse = cognitoClient.adminCreateUser(AdminCreateUserRequest.builder()
                .messageAction(MessageActionType.SUPPRESS)
                .userPoolId(userPoolId)
                .username(signupRequest.getEmail())
                .temporaryPassword(ApplicationContext.TEMP_PASSWORD)
                .build());

        System.out.println("SignupRequestHandler: Created user: " + adminCreateUserResponse);

        return new APIGatewayProxyResponseEvent().withStatusCode(200);
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
