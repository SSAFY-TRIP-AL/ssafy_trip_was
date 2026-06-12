package com.ssafy.tripbaton.global.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String generatePresignedUrl(String folder) {
        String fileName = folder + "/" + UUID.randomUUID();
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10); // 10분

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withExpiration(expiration);

        URL url = amazonS3.generatePresignedUrl(request);
        return url.toString();
    }
}