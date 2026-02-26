package com.wetube.user.service;

import com.wetube.user.dto.UserDto;
import com.wetube.user.dto.UserDtoEntrada;
import com.wetube.user.entity.UserEntity;
import com.wetube.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository){
        this.repository=repository;
    }

    @Override
public UserDto getProfile(Long id){
UserEntity user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario no encontrado"));
return entityToDto(user);
    }

@Override
public UserDto updateProfile(Long id, UserDtoEntrada profileDetails){
    UserEntity user = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario no encontrado"));
    user.setBio(profileDetails.getBio());
    user.setProfilePictureUrl(profileDetails.getProfilePictureUrl());
     repository.save(user);
     return entityToDto(user);
    }

@Override
public UserDto createInitialProfile(Long id, String username){
        UserEntity user=new UserEntity();
    user.setId(id);
    user.setUsername(username);
    user.setCreatedAt(LocalDateTime.now());
     repository.save(user);
     return entityToDto(user);
}

private UserDto entityToDto(UserEntity user){
    return UserDto.builder()
            .username(user.getUsername())
            .bio(user.getBio())
            .profilePictureUrl(user.getProfilePictureUrl())
            .createdAt(user.getCreatedAt())
            .build();
    }

}
