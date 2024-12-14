package edu.rice.wecommunity.util;

import java.io.IOException;
import java.io.InputStream;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

public class S3Util {
    private final static String bucketName = "wecommunity-static-files";

    public static void uploadFile(String fileName, InputStream inputStream)
            throws S3Exception, AwsServiceException, SdkClientException, IOException {
        try (S3Client client = S3Client.builder().region(Region.US_EAST_2).build()) {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType("image/png")
                    .acl("public-read")
                    .build();

            client.putObject(request,
                    RequestBody.fromInputStream(inputStream, inputStream.available()));
        }
    }
}
