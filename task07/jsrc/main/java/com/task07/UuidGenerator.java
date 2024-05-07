package com.task07;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.CloudWatchLogsEvent;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syndicate.deployment.annotations.environment.EnvironmentVariable;
import com.syndicate.deployment.annotations.environment.EnvironmentVariables;
import com.syndicate.deployment.annotations.events.RuleEventSource;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;
import com.task07.service.UUIDGeneratorService;

import java.util.LinkedHashMap;
import java.util.Map;

@LambdaHandler(lambdaName = "uuid_generator",
	roleName = "uuid_generator-role",
	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
@RuleEventSource(targetRule = "uuid_trigger")
@EnvironmentVariables(value = {
		@EnvironmentVariable(key = "region", value = "${region}"),
		@EnvironmentVariable(key = "target_bucket", value = "${target_bucket}")
})
public class UuidGenerator implements RequestHandler<CloudWatchLogsEvent, Map<String, Object>> {
	private static final String S3_BUCKET = System.getenv("target_bucket");
	private static final String REGION = System.getenv("region");

	private static final UUIDGeneratorService service = new UUIDGeneratorService(configureS3Client(),
			configureObjectMapper(),
			S3_BUCKET);

	private static AmazonS3 configureS3Client() {
		return AmazonS3ClientBuilder.standard()
				.withRegion(REGION)
				.build();
	}

	private static ObjectMapper configureObjectMapper() {
		return new ObjectMapper();
	}


	public Map<String, Object> handleRequest(CloudWatchLogsEvent event, Context context) {
		System.out.println("Hello from lambda");
		System.out.println("Region: " + REGION);
		System.out.println("Bucket: " + S3_BUCKET);

		System.out.println("Received event: " + event);

		service.generateUUIDs();

		Map<String, Object> resultMap = new LinkedHashMap<>();
		resultMap.put("statusCode", 200);
		resultMap.put("body", "Hello from Lambda");
		return resultMap;
	}
}
