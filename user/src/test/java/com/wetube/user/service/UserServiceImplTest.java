package com.wetube.user.service;

import com.wetube.user.dto.UserDto;
import com.wetube.user.dto.UserDtoEntrada;
import com.wetube.user.entity.UserEntity;
import com.wetube.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

@Mock
    private UserRepository repository;

@InjectMocks
    private UserServiceImpl service;

@Test
    void getProfile_UserExists_ReturnsUserDto(){
    UserEntity user=UserEntity.builder()
            .id(1L)
            .username("testuser")
            .build();
when(repository.findById(1L)).thenReturn(Optional.of(user));

    UserDto result=service.getProfile(1L);

    //verificaciones
    assertEquals("testuser", result.getUsername());
    verify(repository, times(1)).findById(1L);
}

@Test
    void getProfile_UserNotFound_ThrowsException(){
    //simulamos que el usuario no existe en la db
    when(repository.findById(1L)).thenReturn(Optional.empty());

    //verificar que lanza la esepcion esperada
    assertThrows(ResponseStatusException.class, () -> service.getProfile(1L));
}

@Test
    void shouldThrouNotFound_WhenUserDoesNotExist(){
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    assertThrows(ResponseStatusException.class, () -> {
        service.updateProfile(1L, new UserDtoEntrada("my description", "profile picture"));
    });
}

}
