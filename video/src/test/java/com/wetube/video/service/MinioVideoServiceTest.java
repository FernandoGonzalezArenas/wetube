package com.wetube.video.service;

import com.wetube.video.dto.UploadUrlResponse;
import com.wetube.video.dto.VideoDto;
import com.wetube.video.dto.VideoDtoEntrada;
import com.wetube.video.entity.VideoEntity;
import com.wetube.video.repository.VideoRepository;
import io.minio.MinioClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MinioVideoServiceTest {

@Mock
    private VideoRepository repository;
@Mock
    private InteractionsService interactionsService;
@Mock
    private MinioClient minioClient;
private MinioVideoServiceImpl service;

private Long userId=55L;

@BeforeEach
    void setup(){
    //instanciamos la verdadera implementacion de la clase
    service=new MinioVideoServiceImpl(repository, interactionsService, minioClient);

    //inyectamos valores de value con ReflectionTestUtils
    ReflectionTestUtils.setField(service, "bucketName", "test-bucket");
    ReflectionTestUtils.setField(service, "minioUrl", "http://localhost:9000");

    //agregamos el userId a el contexto de spring
    UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);
}

@Test
    @DisplayName("debe guardar metadata y construir URL correctamente")
    void shouldSaveMetadataAndBuildUrl(){
    VideoDtoEntrada entrada=VideoDtoEntrada.builder()
            .title("mi video")
            .description("descripcion valida")
            .filename("video.mp4")
            .thumbnailUrl("/thumb.jpg")
            .build();


//capturamos lo que se envia al repositorio para verificarlo
ArgumentCaptor<VideoEntity> captor=ArgumentCaptor.forClass(VideoEntity.class);

    VideoDto result=service.saveVideoMetadata(entrada);

    //verificaciones y validaciones
    verify(repository).save(captor.capture());
    VideoEntity savedEntity=captor.getValue();
    assertEquals("mi video", savedEntity.getTitle());
    assertEquals(55L, savedEntity.getUserId());
    //verificamos la logica de url
    assertEquals("http://localhost:9000/test-bucket/videos/video.mp4", savedEntity.getVideoUrl());
    //verificamos el retorno DTO
    assertEquals("mi video", result.getTitle());
}

@Test
    @DisplayName("debe generar una URL prefirmada (UploadUrlResponse)")
    void shouldGenerateUploadUrl() throws Exception{
    //nombre de archivo
    String filename="vacaciones.mp4";

    //se simula la llamada compleja a minioClient
    when(minioClient.getPresignedObjectUrl(any())).thenReturn("http://signed-url.com?signature=xyz");

    UploadUrlResponse response=service.generateUploadUrl(filename);

    //validaciones
    assertNotNull(response);
    assertEquals("http://signed-url.com?signature=xyz", response.uploadUrl());
    assertTrue(response.finalFileName().contains(filename));
}

}
