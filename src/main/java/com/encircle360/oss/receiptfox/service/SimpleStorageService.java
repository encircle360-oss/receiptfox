package com.encircle360.oss.receiptfox.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.Base64;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;

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

    public final static String PDF_EXTENSION = "pdf";

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

        if (!amazonS3Client.doesBucketExistV2(bucket)) {
            createBucketIfNotExists(bucket);
        }
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

    public String pathForNewFile(String fileName) {
        return pathForNewFile(getDefaultBucket(), fileName);
    }

    public String pathForNewFile(String bucket, String fileName) {
        LocalDate localDate = LocalDate.now();
        String monthPath = String.valueOf(localDate.getMonthValue());
        if (monthPath.length() == 1) {
            monthPath = "0" + monthPath;
        }

        Integer year = LocalDate.now().getYear();
        String hash = folderHash(fileName);
        String path = buildPath(year, monthPath, hash, fileName);

        while (exists(bucket, path)) {
            String uuid = UUID.randomUUID().toString();
            path = buildPath(year, monthPath, hash, uuid + "-" + fileName);
        }
        return path;
    }

    private String buildPath(Integer year, String monthPath, String hash, String fileName) {
        return year + "/" + monthPath + "/" + hash + "/" + fileName;
    }

    private String folderHash(String s) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            return null;
        }

        md.update(s.getBytes());
        byte[] digest = md.digest();

        return DatatypeConverter
            .printHexBinary(digest).toLowerCase().substring(0, 2);

    }
}
