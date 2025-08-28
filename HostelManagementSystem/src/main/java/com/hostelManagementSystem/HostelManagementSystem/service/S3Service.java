package com.hostelManagementSystem.HostelManagementSystem.service;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3;
    private final S3Presigner presigner;
    @Value("${aws.s3.bucket}")
    private String bucket;

    public S3Service(@Value("${aws.region}") String region,
                     @Value("${aws.accessKeyId}") String accessKey,
                     @Value("${aws.secretAccessKey}") String secret) {

        AwsBasicCredentials creds = AwsBasicCredentials.create(accessKey, secret);
        Region r = Region.of(region);
        this.s3 = S3Client.builder()
                .region(r)
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();

        this.presigner = S3Presigner.builder()
                .region(r)
                .credentialsProvider(StaticCredentialsProvider.create(creds))
                .build();
    }

    public String uploadFile(MultipartFile file, String keyPrefix) throws IOException {
        String key = keyPrefix + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

        PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PRIVATE)
                .build();

        s3.putObject(por, RequestBody.fromBytes(file.getBytes()));
        return key;
    }

    public URL generatePresignedUrl(String key, Duration validFor) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(validFor)
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presigned = presigner.presignGetObject(presignRequest);
        return presigned.url();
    }

    @PreDestroy
    public void close() {
        presigner.close();
        s3.close();
    }
}
