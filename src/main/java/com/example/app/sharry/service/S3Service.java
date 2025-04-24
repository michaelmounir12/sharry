package com.example.app.sharry.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;
@Service
public class S3Service {

    private final S3Client s3Client;
    private final String bucketName;
    private final Region awsRegion;

    public S3Service(@Value("${aws.accessKeyId}") String accessKeyId,
                     @Value("${aws.secretAccessKey}") String secretAccessKey,
                     @Value("${aws.region}") String region,
                     @Value("${aws.s3.bucket}") String bucketName) {

        this.awsRegion = Region.of(region);

        this.s3Client = S3Client.builder()
                .region(awsRegion)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();

        this.bucketName = bucketName;
    }
    public static String extractKeyFromUrl(String url) {
        try {
            URL s3Url = new URL(url);
            return s3Url.getPath().substring(1);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid S3 URL", e);
        }
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String key = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        Path tempFile = Files.createTempFile("upload-", Objects.requireNonNull(file.getOriginalFilename()));
        file.transferTo(tempFile.toFile());

        s3Client.putObject(request, tempFile);

        Files.deleteIfExists(tempFile);

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, awsRegion.id(), key);
    }

    // 🆕 Add this method
    public BufferedImage getImage(String key) throws IOException {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try (InputStream inputStream = s3Client.getObject(getObjectRequest)) {
            return ImageIO.read(inputStream);
        }
    }
}
