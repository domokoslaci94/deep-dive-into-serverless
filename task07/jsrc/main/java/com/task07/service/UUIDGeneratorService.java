package com.task07.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.task07.api.UUIDWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class UUIDGeneratorService {

    private final AmazonS3 client;
    private final ObjectMapper objectMapper;
    private final String bucketName;

    public UUIDGeneratorService(AmazonS3 client, ObjectMapper objectMapper, String bucketName) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.bucketName = bucketName;
    }

    public void generateUUIDs() {
        String timestamp = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));

        System.out.println("Started process, the following timestamp will be used: " + timestamp);

        List<String> ids = IntStream.range(1, 11)
                .mapToObj(operand -> UUID.randomUUID().toString())
                .collect(Collectors.toList());

        UUIDWrapper uuidWrapper = new UUIDWrapper(ids);

        System.out.println("Generated UUIDs: " + uuidWrapper);

        File tempFile = writeToTempFile(uuidWrapper);

        PutObjectResult putObjectResult = client.putObject(bucketName, timestamp, tempFile);

        System.out.println("Uploaded file: " + putObjectResult + "to bucket: " + bucketName);

    }

    private File writeToTempFile(UUIDWrapper uuidWrapper) {
        File tempFile;
        try {
            tempFile = File.createTempFile("cmtr-2258fa83-", "-test");
            Path tempFilePath = tempFile.toPath();
            byte[] content = objectMapper.writeValueAsString(uuidWrapper).getBytes(StandardCharsets.UTF_8);
            Files.write(tempFilePath, content);
            System.out.println("Written content: " + new String(content, StandardCharsets.UTF_8) + " to file: " + tempFilePath);
        } catch (IOException e) {
            System.out.println("Could not write to file, exception: " + e);
            throw new RuntimeException(e);
        }

        return tempFile;

    }
}
