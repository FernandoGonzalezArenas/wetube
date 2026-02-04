package com.wetube.video.service;

import com.wetube.video.client.CommentsClient;
import com.wetube.video.client.LikesClient;
import com.wetube.video.dto.CommentsDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class InteractionsServiceTest {

    @Mock
    private CommentsClient commentsClient;

    @Mock
    private LikesClient likesClient;

    @InjectMocks
    private InteractionsService service;

    @Test
    @DisplayName("debe retornar comentarios cuando el cliente responde OK")
    void shouldReturnCommentsOnSuccess(){
        when(commentsClient.getCommentsByVideo(1L, null, 10))
                .thenReturn(List.of(CommentsDto.builder().usernameAuthor("user1").content("hola").build()));
        List<CommentsDto> result=service.getCommentsByVideo(1L, null, 10);

        //validaciones
        assertEquals(1, result.size());
        assertEquals("user1", result.get(0).getUsernameAuthor());
    }

    @Test
    @DisplayName("debe retornar fallback manual y retornar lista por defecto ante error")
void shouldExecuteFallbackLogic(){
        List<CommentsDto> result=service.fallbackForComments(1L, null, 10, new RuntimeException("error feign"));

assertEquals(1, result.size());
assertEquals("sistema", result.get(0).getUsernameAuthor());
assertTrue(result.get(0).getContent().contains("temporalmente no disponibles"));
    }

@Test
@DisplayName("debe retornar conteo de likes cuando el cliente responde OK")
void shouldReturnLikesCountOnSuccess(){
        when(likesClient.countLikes(1L)).thenReturn(50L);
        Long likes=service.countLikes(1L);
        assertEquals(50L, likes);
}

@Test
    @DisplayName("debe retornar 0 likes al ejecutar fallback por error")
    void shouldReturnZeroLikesOnFallback(){
        Long likes=service.fallbackForLikes(1L, new RuntimeException("error feign"));
        assertEquals(0L, likes);
}

}
