package com.wetube.video.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wetube.video.dto.*;
import com.wetube.video.service.VideoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@WebMvcTest(VideoController.class)
public class VideoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VideoService videoService;


    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("POST/save-metadata deve validar entrada y retornar 400 si titulo vacío")
    @WithMockUser(username = "123")
    void shouldReturnBadRequestWhenTitleIsBlank() throws  Exception{
        VideoDtoEntrada entradaInvalida=VideoDtoEntrada.builder()
                .title("")
                .description("descripcion valida")
                .build();

        mockMvc.perform(post("/videos/save-metadata")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(entradaInvalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.title").exists());
    }

    @Test
    @DisplayName("GET/upload-url debe verificar token y retornar url firmada")
    @WithMockUser(username = "100")
    void shouldGetUploadUrlWithAuth() throws  Exception{
        when(videoService.generateUploadUrl(anyString()))
                .thenReturn(new UploadUrlResponse("http://minio/url", "uuid-video.mp4"));

        mockMvc.perform(get("/videos/upload-url")
                        .param("filename", "mivideo.mp4"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uploadUrl").value("http://minio/url"));
    }

    @Test
    @DisplayName("GET /upload-url - debe retornar 401 si el usuario no esta autenticado")
    void shouldReturn401WhenAnonymous() throws Exception{
        mockMvc.perform(get("/videos/upload-url")
                .param("filename", "mivideo.mp4"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /interactions/{videoId} debe retornar interacciones con paginacion")
    @WithMockUser(username = "1")
    void shouldGetInteractionsWithPagination() throws Exception{
        when(videoService.getInteractions(1L, 100L, 5))
                .thenReturn(new InteractionsDto(Collections.emptyList(), 10L));

        mockMvc.perform(get("/videos/interactions/1")
                .param("lastId", "100")
                .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likes").value(10));
    }

@Test
    @DisplayName("GET /videos/{videoId}/play debe retornar 200 y el DTO de reproduccion")
    @WithMockUser
    void shouldReturnPlaybackData() throws Exception{
    VideoPlaybackDto playback=VideoPlaybackDto.builder().videoUrl("http://signed.com").build();
    when(videoService.getVideoForPlayback(1L)).thenReturn(playback);

    mockMvc.perform(get("/videos/1/play"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.videoUrl").value("http://signed.com"));
    }

    @Test
    @DisplayName("POST videos/list-likes debe retornar lista de videos por IDs")
    @WithMockUser
    void shouldGetVideosBySpecificIds() throws Exception {
        IdsDto ids = new IdsDto(List.of(1L, 2L));
        when(videoService.getVideosByIds(any())).thenReturn(Collections.emptyList());

mockMvc.perform(post("/videos/list-likes")
        .with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(ids)))
        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /videos/my-feed-subs debe retornar feed de subscripciones")
    @WithMockUser
    void shouldReturnsSubsFeed() throws Exception{
        when(videoService.getSubscriptionsFeed()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/videos/my-feed-subs"))
                .andExpect(status().isOk());
    }

}
