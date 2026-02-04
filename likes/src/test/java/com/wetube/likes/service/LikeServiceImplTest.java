package com.wetube.likes.service;

import com.wetube.likes.Service.LikeServiceImpl;
import com.wetube.likes.dto.VideoLikeStatusDto;
import com.wetube.likes.entity.LikeEntity;
import com.wetube.likes.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LikeServiceImplTest {

@Mock
    private LikeRepository repository;
@InjectMocks
    private LikeServiceImpl service;

private Long videoId=100L;
private Long userId=1L;

@BeforeEach
void setupSecurityContext(){
    //simulamos que Spring Security ya tiene al usuario autenticado
    UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
    SecurityContextHolder.getContext().setAuthentication(auth);
}

@Test
    void toggleLike_ShouldCreateLike_WhenNotExists(){
    //el usuario esta autenticado y no ha dado like antes
    when(repository.findByUserIdAndVideoId(userId, videoId)).thenReturn(Optional.empty());

    //ejecutar el toggle
    boolean resultado=service.toggleLike(videoId);

    //validaciones (debe devolver true y haver llamado a save una vez)
    assertTrue(resultado);
    verify(repository, times(1)).save(any(LikeEntity.class));
}

@Test
    void toggleLike_ShouldDeleteLike_WhenAlreadyExists(){
    //dar like agregando registro a la base de datos
    LikeEntity existingLike=LikeEntity.builder()
            .id(1L)
            .userId(userId)
            .videoId(videoId)
            .build();
    when(repository.findByUserIdAndVideoId(userId, videoId)).thenReturn(Optional.of(existingLike));

    //quitar el like llamando a el toggle
    boolean resultado=service.toggleLike(videoId);

    //validaciones (el resultado debe ser false y llamar a delete una vez)
    assertFalse(resultado);
    verify(repository, times(1)).delete(existingLike);
}

@Test
    void getVideoLikeStatus_ShouldReturnCorrectStatus_ForAuthenticatedUser(){
    when(repository.countByVideoId(videoId)).thenReturn(15L);
    when(repository.existsByUserIdAndVideoId(userId, videoId)).thenReturn(true);

    //llamada a el metodo
    VideoLikeStatusDto status=service.getVideoLikeStatus(videoId);

    //validaciones
    assertEquals(15L, status.getTotalLikes());
    assertTrue(status.isLikedByUser());
}

@Test
    void getVideoLikeStatus_ShouldReturnFalse_ForGuestUser(){
//limpeamos el contexto para simular que no hay nadie logeado
    SecurityContextHolder.clearContext();

    when(repository.countByVideoId(videoId)).thenReturn(15L);

    VideoLikeStatusDto status=service.getVideoLikeStatus(videoId);

    //validaciones
    assertEquals(15L, status.getTotalLikes());
    assertFalse(status.isLikedByUser());
}

}
