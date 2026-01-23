package com.wetube.likes.controller;

import com.wetube.likes.Service.LikeService;
import com.wetube.likes.dto.VideoLikeStatusDto;
import com.wetube.likes.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
public class LikeControllerTest {

@Autowired
    private MockMvc mockMvc;
@MockBean
    private LikeService service;
@MockBean
private JwtUtil jwtUtil;

@Test
    @WithMockUser
    void toggleLike_ShouldReturnCreated_WhenTrue() throws Exception{
    when(service.toggleLike(eq(1L))).thenReturn(true);

    mockMvc.perform(post("/like/1/toggle").with(csrf()))
            .andExpect(status().isCreated());
}

@Test
    @WithMockUser
    void toggleLike_ShouldReturnNoContent_WhenFalse() throws Exception{
    when(service.toggleLike(eq(1L))).thenReturn(false);

    mockMvc.perform(post("/like/1/toggle").with(csrf()))
            .andExpect(status().isNoContent());
}

@Test
@WithMockUser
    void getLikeStatus_ShouldReturnOkWithJson() throws Exception{
    VideoLikeStatusDto status=new VideoLikeStatusDto(10L, true);
    when(service.getVideoLikeStatus(eq(1L))).thenReturn(status);

    mockMvc.perform(get("/like/1/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalLikes").value(10))
            .andExpect(jsonPath("$.likedByUser").value(true));
}

}
