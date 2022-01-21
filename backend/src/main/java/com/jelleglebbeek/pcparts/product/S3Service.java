package com.jelleglebbeek.pcparts.product;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.jelleglebbeek.pcparts.exceptions.UnsupportedMediaFormatException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class S3Service {

    private AmazonS3 client;

    @Value("${S3.endpoint.url}")
    private String endpoint;

    @Value("${S3.endpoint.region}")
    private String region;

    @Value("${S3.bucket}")
    private String bucket;

    @Value("${S3.prefix}")
    private String prefix;

    @Value("${S3.credentials.access-key}")
    private String accessKey;

    @Value("${S3.credentials.secret-key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );

        EndpointConfiguration endpointConfiguration = new EndpointConfiguration(endpoint, region);

        client = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withEndpointConfiguration(endpointConfiguration)
                .build();
    }

    public String upload(byte[] data, UUID productId) {
        InputStream inputStream = new ByteArrayInputStream(data);

        String path =  prefix + "/" + productId;
        String contentType = getContentType(data);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(data.length);
        objectMetadata.setContentType(contentType);

        client.putObject(bucket, path, inputStream, objectMetadata);

        AccessControlList acl = client.getObjectAcl(bucket, path);
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read);
        client.setObjectAcl(bucket, path, acl);

        return "https://" + bucket + "." + endpoint + "/" + path;
    }

    public void remove(UUID productId) {
        client.deleteObject(bucket, prefix + "/" + productId);
    }

    public String getContentType(byte[] bytes) {
        Tika tika = new Tika();
        String detectedType = tika.detect(bytes);
        if (!detectedType.contains("image")) {
            throw new UnsupportedMediaFormatException("The format: " + detectedType + " is not supported.");
        }
        return detectedType;
    }

}
