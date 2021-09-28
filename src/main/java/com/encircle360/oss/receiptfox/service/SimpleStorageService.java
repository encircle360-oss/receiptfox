package com.encircle360.oss.receiptfox.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class SimpleStorageService {

    private AmazonS3 amazonS3Client;

    private final Base64.Encoder encoder = Base64.getEncoder();

    private final Base64.Decoder decoder = Base64.getDecoder();

    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.sigv4}")
    private String sigv4;

    @Value("${s3.accessKey}")
    private String accessKey;

    @Value("${s3.secretKey}")
    private String secretKey;

    @Value("${s3.bucket}")
    private String bucket;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(credentials);
        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, sigv4);

        amazonS3Client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(credentialsProvider)
            .withEndpointConfiguration(endpointConfiguration)
            .withPathStyleAccessEnabled(true)
            .build();
    }

    public boolean save(String bucket, String path, byte[] data, String mimeType) {
        createBucketIfNotExists(bucket);

        ByteArrayInputStream byteDataInputStream = new ByteArrayInputStream(data);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(data.length);
        objectMetadata.setContentType(mimeType);

        amazonS3Client.putObject(bucket, path, byteDataInputStream, objectMetadata);
        return amazonS3Client.doesObjectExist(bucket, path);
    }

    public boolean save(String path, byte[] data, String mimeType) {
        return save(getDefaultBucket(), path, data, mimeType);
    }

    public byte[] get(String bucket, String path) throws IOException {
        S3Object file;
        try {
            file = amazonS3Client.getObject(bucket, path);
        } catch (AmazonS3Exception amazonS3Exception) {
            return null;
        }

        S3ObjectInputStream inputStream = file.getObjectContent();

        return StreamUtils.copyToByteArray(inputStream);
    }

    public byte[] get(String path) throws IOException {
        return get(getDefaultBucket(), path);
    }

    public void delete(String bucket, String path) {
        amazonS3Client.deleteObject(bucket, path);
    }

    public void delete(String path) {
        delete(getDefaultBucket(), path);
    }

    public String getDefaultBucket() {
        return bucket;
    }

    public boolean exists(String bucket, String path) {
        return amazonS3Client.doesObjectExist(bucket, path);
    }

    public boolean exists(String path) {
        return exists(getDefaultBucket(), path);
    }

    public void createBucketIfNotExists(String bucket) {
        if (amazonS3Client.doesBucketExistV2(bucket)) {
            return;
        }
        amazonS3Client.createBucket(bucket);
    }

    public String createFileId() {
        return UUID.randomUUID().toString();
    }

    public byte[] decode(final String data) {
        return decoder.decode(data);
    }

    public byte[] encode(final byte[] data) {
        return encoder.encode(data);
    }

    public String path(String fileName) {
        LocalDate localDate = LocalDate.now();
        String monthPath = String.valueOf(localDate.getMonthValue());
        if (monthPath.length() == 1) {
            monthPath = "0" + monthPath;
        }

        String hash = pathHash(fileName);
        return localDate.getYear() + "/" + monthPath + "/" + hash + "/" + fileName;
    }

    private String pathHash(String s) {
        String md5 = MD5Encoder.encode(s.getBytes(StandardCharsets.UTF_8));
        return md5.toLowerCase().substring(0, 1);
    }
}
