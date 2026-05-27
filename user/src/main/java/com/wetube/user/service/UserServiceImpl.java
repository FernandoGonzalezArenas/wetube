package com.wetube.user.service;

import com.wetube.user.dto.UploadUrlResponse;
import com.wetube.user.dto.UserDto;
import com.wetube.user.dto.UserDtoEntrada;
import com.wetube.user.dto.UserPrincipal;
import com.wetube.user.entity.UserEntity;
import com.wetube.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
private final StorageService storageService;

    @Override
public UserDto getProfile(Long id){
UserEntity user = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario no encontrado"));
return entityToDto(user);
    }

    @Override
    public UploadUrlResponse getUploadUrl(String filename){
        return storageService.generateUploadUrl(filename);
    }

@Override
public UserDto updateProfile( UserDtoEntrada profileDetails){
    UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Long id=principal.userId();

    UserEntity user = repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario no encontrado"));
    user.setBio(profileDetails.getBio());
    user.setProfilePictureUrl(profileDetails.getProfilePictureUrl());
    user.setPrivacyLikes(profileDetails.getPrivacyLikes());
    user.setPrivacySubs(profileDetails.getPrivacySubs());
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
        String foto=user.getProfilePictureUrl();
        String finalUrl=(foto!=null) ?storageService.getPublicUrl(foto) : null;
    return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .bio(user.getBio())
            .profilePictureUrl(finalUrl)
            .privacyLikes(user.getPrivacyLikes())
            .privacySubs(user.getPrivacySubs())
            .createdAt(user.getCreatedAt())
            .build();
    }

    @Override
    @Transactional
    public void banUserInternal(Long userId){
        //obtenemos los datos de el usuario que hace la peticion
        Authentication auth= SecurityContextHolder.getContext().getAuthentication();
        //verificamos si es admin
        boolean isAdmin=auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "no tienes permisos de administrador para borrar la cuenta");
        }

        if (!repository.existsById(userId)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "el usuario que se quiere banear no existe");
        }
        repository.deleteById(userId);
    }

}
