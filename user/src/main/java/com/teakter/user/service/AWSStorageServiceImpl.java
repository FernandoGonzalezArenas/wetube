package com.teakter.user.service;

import com.teakter.user.dto.UploadUrlResponse;
import com.teakter.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "storage.type", havingValue = "aws")
public class AWSStorageServiceImpl implements StorageService{

private final S3Presigner s3Presigner;

@Value("${aws.s3.bucket-users}")
    private String bucketName;

@Value("${aws.cloudfront.domain.users}")
private String cloudfrontDomainUsers;

@Override
    public UploadUrlResponse generateUploadUrl(String filename){
    String finalFilename= UUID.randomUUID()+"-"+filename;
    String objectName="profiles/"+finalFilename;
    PutObjectPresignRequest presignRequest=PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ZERO.ofMinutes(15))
            .putObjectRequest(req -> req.bucket(bucketName).key(objectName))
            .build();

    PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
    return new UploadUrlResponse(presignedRequest.url().toString(), finalFilename);
}

@Override
    public String getPublicUrl(String filename){
    return "https://"+cloudfrontDomainUsers+"/profiles/"+filename;
}

}
