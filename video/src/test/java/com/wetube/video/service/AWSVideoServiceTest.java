package com.wetube.video.service;

import com.wetube.video.dto.*;
import com.wetube.video.entity.VideoEntity;
import com.wetube.video.repository.VideoRepository;
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
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AWSVideoServiceTest {

@Mock
    private VideoRepository repository;
@Mock
    private InteractionsService interactionsService;
@Mock
    private S3Presigner s3Presigner;
@Mock
    private PresignedGetObjectRequest presignedGetObjectRequest;
@Mock
private PresignedPutObjectRequest presignedPutObjectRequest;

private AWSVideoServiceImpl service;
private final Long userId=1L;

@BeforeEach
    void setup(){
    service=new AWSVideoServiceImpl(repository, interactionsService, s3Presigner);
    ReflectionTestUtils.setField(service, "bucketName", "aws-bucket-videos");

    UserPrincipal principal=new UserPrincipal(userId, "user-aws");
    UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(principal, null, Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);
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

    service.saveVideoMetadata(entrada);

    ArgumentCaptor<VideoEntity> captor=ArgumentCaptor.forClass(VideoEntity.class);

//validaciones
    verify(repository).save(captor.capture());
    VideoEntity entity=captor.getValue();

    assertEquals(1L, entity.getUserId());
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

@Test
    @DisplayName("aws: debe retornar VideoPlaybackDto con URL firmada de GetObject")
    void shouldGetVideoForPlaybackAWS() throws Exception{
VideoEntity video=VideoEntity.builder()
        .id(10L)
        .title("video AWS")
        .videoUrl("https://s3.aws.com/videos/clip-123.mp4")
        .build();
    String fakeSignedUrl = "https://aws-bucket.s3.amazonaws.com/videos/clip-123.mp4?X-Amz-Signature=xyz";

    when(repository.findById(10L)).thenReturn(Optional.of(video));
    when(s3Presigner.presignGetObject(any(GetObjectPresignRequest.class))).thenReturn(presignedGetObjectRequest);
    when(presignedGetObjectRequest.url()).thenReturn(new URL(fakeSignedUrl));

    VideoPlaybackDto result=service.getVideoForPlayback(10L);

    assertNotNull(result);
    assertEquals(fakeSignedUrl, result.getVideoUrl());
    verify(s3Presigner).presignGetObject(any(GetObjectPresignRequest.class));
}

@Test
    @DisplayName("AWS: debe retornar 404 cuando el video no exista para reproduccion")
    void shouldThrowNotFoundWhenVideoDoesNotExist(){
when(repository.findById(99L)).thenReturn(Optional.empty());

assertThrows(ResponseStatusException.class, () -> service.getVideoForPlayback(99L));
}

@Test
    @DisplayName("debe obtener el feed de subscripciones basado en el usuario autenticado")
    void shouldGetSubscriptionsFeedAWS(){
    List<Long> followedChannels=List.of(100L, 200L);
when(interactionsService.getSubscriptionsByUser(anyLong())).thenReturn(followedChannels);

VideoEntity video=VideoEntity.builder()
                .title("video subs")
                        .description("desc")
                                .videoUrl("url")
                                        .thumbnailUrl("thumb")
                                                .build();
when(repository.findByUserIdInOrderByCreatedAtDesc(followedChannels))
        .thenReturn(List.of(video));

List<VideoDto> result=service.getSubscriptionsFeed();

assertEquals(1, result.size());
verify(interactionsService).getSubscriptionsByUser(anyLong());
verify(repository).findByUserIdInOrderByCreatedAtDesc(followedChannels);
}

}
