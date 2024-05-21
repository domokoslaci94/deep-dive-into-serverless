package com.task10.web.user;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.task10.web.InternalRequestHandler;
import com.task10.web.user.api.SignupRequest;
import com.task10.web.util.ObjectMapperDecorator;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.Map;
import java.util.regex.Pattern;

public class SignupInternalRequestHandler implements InternalRequestHandler {

    private static final String TEMP_PASSWORD = "TempPassword123!";
    private static final String REQUEST_METHOD = "POST";
    private static final String REQUEST_PATH = "^/signup$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^[A-Za-z0-9$%^*\\-_]{12,}$");

    private final ObjectMapperDecorator objectMapper;
    private final CognitoIdentityProviderClient cognitoClient;
    private final String userPoolId;
    private final String clientId;

    public SignupInternalRequestHandler(ObjectMapperDecorator objectMapper, CognitoIdentityProviderClient cognitoClient, String userPoolId, String clientId) {
        this.objectMapper = objectMapper;
        this.cognitoClient = cognitoClient;
        this.userPoolId = userPoolId;
        this.clientId = clientId;
    }

    private static void validateSignupRequest(SignupRequest signupRequest) {
        validateEmail(signupRequest.getEmail());
        validatePassword(signupRequest.getPassword());
    }

    private static void validatePassword(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new IllegalArgumentException("Invalid password");
        }
    }

    private static void validateEmail(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    @Override
    public APIGatewayProxyResponseEvent handle(APIGatewayProxyRequestEvent request) {
        System.out.println("SignupRequestHandler: Received request: " + request);
        SignupRequest signupRequest = objectMapper.readValue(request.getBody(), SignupRequest.class);

        validateSignupRequest(signupRequest);

        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();

        AdminCreateUserResponse adminCreateUserResponse = cognitoClient.adminCreateUser(AdminCreateUserRequest.builder()
                .messageAction(MessageActionType.SUPPRESS)
                .userPoolId(userPoolId)
                .username(email)
                .temporaryPassword(TEMP_PASSWORD)
                .userAttributes(AttributeType.builder()
                                .name("email")
                                .value(email)
                                .build(),
                        AttributeType.builder()
                                .name("email_verified")
                                .value("true")
                                .build())
                .build());

        System.out.println("SignupRequestHandler: Created user: " + adminCreateUserResponse);

        AdminInitiateAuthResponse adminInitiateAuthResponse = cognitoClient.adminInitiateAuth(AdminInitiateAuthRequest.builder()
                .userPoolId(userPoolId)
                .clientId(clientId)
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .authParameters(Map.of(
                        "USERNAME", email,
                        "PASSWORD", TEMP_PASSWORD))
                .build());

        System.out.println("SignupRequestHandler: Authenticated user: " + adminInitiateAuthResponse);

        RespondToAuthChallengeResponse respondToAuthChallengeResponse = cognitoClient.respondToAuthChallenge(RespondToAuthChallengeRequest.builder()
                .challengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .clientId(clientId)
                .challengeResponses(Map.of(
                        "USERNAME", email,
                        "NEW_PASSWORD", password))
                .session(adminInitiateAuthResponse.session())
                .build());

        System.out.println("SignupRequestHandler: Responded to auth challenge: " + respondToAuthChallengeResponse);

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
