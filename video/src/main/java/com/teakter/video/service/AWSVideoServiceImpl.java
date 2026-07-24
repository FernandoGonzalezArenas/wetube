package com.teakter.video.service;

import com.teakter.video.dto.UploadUrlResponse;
import com.teakter.video.repository.VideoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "aws")
public class AWSVideoServiceImpl extends AbstractVideoService {

    private final S3Presigner s3Presigner;

@Value("${aws.s3.bucket-videos}")
    private String bucketName;

    public AWSVideoServiceImpl(VideoRepository videoRepository, InteractionsService interactionsService, LikeService likeService, SubscriptionService subscriptionService, UserService userService, RabbitTemplate rabbitTemplate, S3Presigner s3Presigner){
    super(videoRepository, interactionsService, likeService, subscriptionService, userService, rabbitTemplate);
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

    //metodo para obtener la URL prefirmada de AWS S3 para miniaturas
    @Override
    public UploadUrlResponse generateUploadUrlThumb(String filename){
        String finalFileName=UUID.randomUUID().toString() + "-" + filename;
        String objectName="thumbnails/" + finalFileName;
        PutObjectPresignRequest presignRequest=PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ZERO.ofMinutes(15))
                .putObjectRequest(req -> req.bucket(bucketName).key(objectName))
                .build();

        PresignedPutObjectRequest presignedRequest= s3Presigner.presignPutObject(presignRequest);
        return new UploadUrlResponse(presignedRequest.url().toString(), finalFileName);
    }

    @Override
    protected String buildFullThumbnailUrl(String filename){
        return "https://" + bucketName + ".s3.amazonaws.com/thumbnails/" + filename;
    }

    @Override
protected String getPlaybackUrl(String filename){
        //extraemos el nombre de el objeto
    String key="videos/"+filename;

    try {
        GetObjectPresignRequest getObjectPresignRequest=GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofHours(2))
                .getObjectRequest(req -> req.bucket(bucketName).key(key))
                .build();

        return s3Presigner.presignGetObject(getObjectPresignRequest).url().toString();
    }catch (Exception e){
logger.error("error generando URL de reproduccion AWS: {}", e.getMessage());
throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "el servicio esta temporalmente no disponible");
    }
}

}
