package com.task10.web.user;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task10.config.ApplicationContext;
import com.task10.web.InternalRequestHandler;
import com.task10.web.user.api.SigninRequest;
import com.task10.web.user.api.SigninResponse;
import com.task10.web.util.ObjectMapperDecorator;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;

public class SigninInternalRequestHandler implements InternalRequestHandler {

    private static final String REQUEST_METHOD = "POST";
    private static final String REQUEST_PATH = "^/signin$";

    private final ObjectMapperDecorator objectMapper;
    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;
    private final String clientId;

    public SigninInternalRequestHandler(ObjectMapperDecorator objectMapper, CognitoIdentityProviderClient cognitoClient, String userPoolId, String clientId) {
        this.objectMapper = objectMapper;
        this.cognitoClient = cognitoClient;
        this.userPoolId = userPoolId;
        this.clientId = clientId;
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.println("SigninRequestHandler: Received request: " + request);

        SigninRequest signinRequest = objectMapper.readValue(request.getBody(), SigninRequest.class);

        AdminInitiateAuthResponse adminInitiateAuthResponse = cognitoClient.adminInitiateAuth(AdminInitiateAuthRequest.builder()
                .userPoolId(userPoolId)
                .clientId(clientId)
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .authParameters(Map.of(
                        "USERNAME", signinRequest.getEmail(),
                        "PASSWORD", ApplicationContext.TEMP_PASSWORD))
                .build());

        System.out.println("SigninRequestHandler: Authenticated user: " + adminInitiateAuthResponse);

        RespondToAuthChallengeResponse respondToAuthChallengeResponse = cognitoClient.respondToAuthChallenge(RespondToAuthChallengeRequest.builder()
                .challengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .clientId(clientId)
                .challengeResponses(Map.of(
                        "USERNAME", signinRequest.getEmail(),
                        "NEW_PASSWORD", signinRequest.getPassword()))
                .session(adminInitiateAuthResponse.session())
                .build()
        );

        System.out.println("SigninRequestHandler: Responded to auth challenge: " + respondToAuthChallengeResponse);

        return new APIGatewayProxyResponseEvent()
                .withBody(objectMapper.writeValueAsString(new SigninResponse(respondToAuthChallengeResponse.authenticationResult().idToken())))
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
