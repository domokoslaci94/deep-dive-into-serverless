package com.task08;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaLayer;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.DeploymentRuntime;
import com.syndicate.deployment.model.RetentionSetting;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@LambdaHandler(lambdaName = "api_handler",
        roleName = "api_handler-role",
        layers = {"sdk-layer"},
        logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@LambdaLayer(
        layerName = "sdk-layer",
        libraries = {"lib/commons-lang3-3.14.0.jar"},
        runtime = DeploymentRuntime.JAVA11
)
@LambdaUrlConfig
public class ApiHandler implements RequestHandler<Object, String> {

    private static final String OPEN_METEO_API = "https://api.open-meteo.com/v1/forecast" +
            "?latitude=52.52&longitude=13.41" +
            "&current=temperature_2m,wind_speed_10m" +
            "&hourly=temperature_2m,relative_humidity_2m,wind_speed_10m";

	private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();

    private static HttpResponse<String> sendRequest(HttpRequest httpRequest) {
        HttpResponse<String> response;
        try {
            response = HTTP_CLIENT.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return response;
    }

    private static HttpRequest generateRequest() {
        URI targetUri;
        try {
            targetUri = new URI(OPEN_METEO_API);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        return HttpRequest.newBuilder()
                .GET()
                .uri(targetUri)
                .build();
    }

    public String handleRequest(Object request, Context context) {
        System.out.println("Hello from" + StringUtils.capitalize("lambda"));

        HttpRequest httpRequest = generateRequest();

		return sendRequest(httpRequest).body();
    }
}
