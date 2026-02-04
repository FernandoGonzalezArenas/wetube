package com.wetube.video.service;

import com.wetube.video.dto.UploadUrlResponse;
import com.wetube.video.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "aws")
public class AWSVideoServiceImpl extends AbstractVideoService {

    private final S3Presigner s3Presigner;

@Value("${aws.s3.bucket-name}")
    private String bucketName;

    public AWSVideoServiceImpl(VideoRepository videoRepository, InteractionsService interactionsService, S3Presigner s3Presigner){
    super(videoRepository, interactionsService);
    this.s3Presigner=s3Presigner;
}

//metodo para obtener la URL prefirmada de AWS S3
@Override
    public UploadUrlResponse generateUploadUrl(String filename){
String finalFileName=UUID.randomUUID().toString() + "-" + filename;
String objectName="videos/" + finalFileName;
        PutObjectPresignRequest presignRequest=PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ZERO.ofMinutes(15))
                .putObjectRequest(req -> req.bucket(bucketName).key(objectName))
                .build();

        PresignedPutObjectRequest presignedRequest= s3Presigner.presignPutObject(presignRequest);
        return new UploadUrlResponse(presignedRequest.url().toString(), finalFileName);
    }

    @Override
    protected String buildFullVideoUrl(String filename){
    return "https://" + bucketName + ".s3.amazonaws.com/videos/" + filename;
    }
}
