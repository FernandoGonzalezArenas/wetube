package com.wetube.video.service;

import com.wetube.video.dto.UploadUrlResponse;
import com.wetube.video.repository.VideoRepository;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@ConditionalOnProperty(name = "storage.type", havingValue = "minio")
public class MinioVideoServiceImpl extends AbstractVideoService{

private final MinioClient minioClient;
private static final Logger logger= LoggerFactory.getLogger(MinioVideoServiceImpl.class);

@Value("${minio.bucket-name}")
    private String bucketName;

@Value("${minio.url}")
private String minioUrl;

    public MinioVideoServiceImpl(VideoRepository videoRepository, InteractionsService interactionsService, MinioClient minioClient){
    super(videoRepository, interactionsService);
    this.minioClient=minioClient;
}

//metodo para generar una URL firmada para subir videos a MinIO
    @Override
    public UploadUrlResponse generateUploadUrl(String filename){
        String finalFileName=UUID.randomUUID().toString()+"-"+filename;
        String objectName="videos/"+finalFileName;
    try {
        //configurar la solicitud de URL firmada
String presignedUrl =minioClient.getPresignedObjectUrl(
        GetPresignedObjectUrlArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .method(Method.PUT)
                .expiry(15, TimeUnit.MINUTES)
                .build()
);
return new UploadUrlResponse(presignedUrl, finalFileName);
    }catch (Exception e){
        e.printStackTrace();
throw new RuntimeException("error al generar URL firmada"+e.getMessage());
    }
    }

    @Override
    protected String buildFullVideoUrl(String filename){
return minioUrl + "/" + bucketName + "/videos/" + filename;
    }


}
