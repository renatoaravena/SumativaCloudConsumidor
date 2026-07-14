package com.duoc.SumativaCloudConsumidor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class AwsS3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    // Spring Cloud AWS inyecta el S3Client autoconfigurado, no lo creamos nosotros
    public AwsS3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public void uploadBytes(String key, byte[] content) {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("application/pdf")
                .build(),
            RequestBody.fromBytes(content)
        );
    }

    public void deleteObject(String key) {
        s3Client.deleteObject(
            DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build()
        );
    }
}