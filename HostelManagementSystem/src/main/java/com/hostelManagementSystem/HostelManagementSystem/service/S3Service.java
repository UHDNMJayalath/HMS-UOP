package com.hostelManagementSystem.HostelManagementSystem.service;

import jakarta.annotation.PreDestroy;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
// ... (omitted imports) ...
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

// ... (omitted imports) ...

@Service
public class S3Service {

    private final S3Client s3;
    private final S3Presigner presigner;
    @Value("${aws.s3.bucket}")
    private String bucket;


    private final String region;

    public S3Service(@Value("${aws.region}") String region,
                     @Value("${aws.accessKeyId}") String accessKey,
                     @Value("${aws.secretAccessKey}") String secret) {

        this.region = region;
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
        return key; // Returns S3 Key (file path)
    }

    // =========================================================
    // NEW METHOD: Upload with a Custom File Name
    // =========================================================
    public String uploadFileWithCustomName(MultipartFile file, String keyPrefix, String customFileName) throws IOException {
        // 1. Get original extension (e.g., .pdf, .png)
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 2. Construct Key using Custom Name + Extension
        // Result ex: payment-slips/S-18-500_HostelFee.pdf
        String key = keyPrefix + "/" + customFileName + extension;

        PutObjectRequest por = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType(file.getContentType())
                .acl(ObjectCannedACL.PRIVATE)
                .build();

        s3.putObject(por, RequestBody.fromBytes(file.getBytes()));
        return key; // Returns S3 Key
    }

    public String getBucketName() { // <<< Getter for Bucket Name
        return bucket;
    }

    public String getRegion() { // <<< Getter for Region
        return region;
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