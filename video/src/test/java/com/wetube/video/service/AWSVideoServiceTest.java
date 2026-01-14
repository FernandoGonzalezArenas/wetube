package com.wetube.video.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

import com.wetube.video.dto.UploadUrlResponse;
import com.wetube.video.dto.VideoDtoEntrada;
import com.wetube.video.entity.VideoEntity;
import com.wetube.video.repository.VideoRepository;
import com.wetube.video.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;

@ExtendWith(MockitoExtension.class)
public class AWSVideoServiceTest {

@Mock
    private VideoRepository repository;
@Mock
    private InteractionsService interactionsService;
@Mock
    private JwtUtil jwtUtil;
@Mock
    private S3Presigner s3Presigner;
@Mock
    private HttpServletRequest request;
@Mock
    private PresignedPutObjectRequest presignedPutObjectRequest;

private AWSVideoServiceImpl service;

@BeforeEach
    void setup(){
    service=new AWSVideoServiceImpl(repository, interactionsService, jwtUtil, s3Presigner);
    ReflectionTestUtils.setField(service, "bucketName", "aws-bucket-videos");
}

@Test
    @DisplayName("AWS: debe guardar metadata y construir la url prefirmada correctamente")
    void shouldSaveMetadataAndBuildUrlAWS(){
    VideoDtoEntrada entrada=VideoDtoEntrada.builder()
            .title("video AWS")
            .description("descripcion valida")
            .filename("clip.mp4")
            .thumbnailUrl("img.jpg")
            .build();

when(jwtUtil.getUseridOrThrow(request)).thenReturn(99L);
ArgumentCaptor<VideoEntity> captor=ArgumentCaptor.forClass(VideoEntity.class);
service.saveVideoMetadata(entrada, request);

//validaciones
    verify(repository).save(captor.capture());
    VideoEntity entity=captor.getValue();

//validamos la logica exacta de construccion de url de AWS
    assertEquals("https://aws-bucket-videos.s3.amazonaws.com/videos/clip.mp4", entity.getVideoUrl());
}

@Test
    @DisplayName("AWS: debe generar URL prefirmada")
    void shouldGenerateUploadUrlAWS() throws  Exception{
String filename="test.mp4";
    String fakeUrl = "https://aws-bucket.s3.amazonaws.com/videos/uuid-test.mp4?signature=123";

//simulamos la cadena de llamadas de AWS SDK v2
    when(s3Presigner.presignPutObject(any(PutObjectPresignRequest.class))).thenReturn(presignedPutObjectRequest);
    when(presignedPutObjectRequest.url()).thenReturn(new URL(fakeUrl));

    UploadUrlResponse response= service.generateUploadUrl(filename);
    assertNotNull(response);
    assertEquals(fakeUrl, response.uploadUrl());
assertTrue(response.finalFileName().contains(filename));
}

}
