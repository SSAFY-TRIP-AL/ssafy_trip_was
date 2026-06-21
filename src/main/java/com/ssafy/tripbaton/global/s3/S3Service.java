package com.ssafy.tripbaton.global.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public Map<String, String> generatePresignedUrl(UploadType type) {
        String folder;

        switch (type) {
            case PROFILE:
                folder = "profiles";
                break;
            case RELAY:
                folder = "relays";
                break;
            default:
                throw new IllegalArgumentException("잘못된 업로드 타입입니다.");
        }

        // S3 객체 키 생성
        String key = folder + "/" + UUID.randomUUID();

        // 만료 시간 (10분)
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 10);

        // Presigned URL 생성
        GeneratePresignedUrlRequest request =
                new GeneratePresignedUrlRequest(bucket, key)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        URL presignedUrl = amazonS3.generatePresignedUrl(request);

        // 업로드 완료 후 접근할 실제 이미지 URL
        String imageUrl = amazonS3.getUrl(bucket, key).toString();

        return Map.of(
                "presignedUrl", presignedUrl.toString(),
                "imageUrl", imageUrl
        );

    }
}