package com.wetube.user.controller;

import com.wetube.user.dto.UserDto;
import com.wetube.user.dto.UserDtoEntrada;
import com.wetube.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

@Autowired
    private MockMvc mockMvc;

@MockBean
    private UserService userService;

@Test
    void getPublicProfile_ReturnsUser() throws Exception{
    UserDto user=UserDto.builder()
            .username("pedro")
            .bio("hola")
            .build();
when(userService.getProfile(1L)).thenReturn(user);

mockMvc.perform(get("/users/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("pedro"));
}

@Test
    void updateMyProfile_ReturnsUpdatedUsser() throws Exception{
    UserDto update=UserDto.builder()
            .username("raul")
            .bio("nueva bio")
            .build();
    when(userService.updateProfile(anyLong(), any())).thenReturn(update);

mockMvc.perform(put("/users/me")
        .header("X-User-Id", 1L)
        .contentType("application/json")
        .content("{\"bio\": \"nueva bio\", \"profilePictureUrl\": \"http://foto.jpg\"}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.bio").value("nueva bio"));
verify(userService, times(1)).updateProfile(eq(1L), any(UserDtoEntrada.class));
}

}
