package com.wetube.user.service;

import com.wetube.user.dto.UserDto;
import com.wetube.user.dto.UserDtoEntrada;
import com.wetube.user.entity.UserEntity;

public interface UserService {

UserDto getProfile(Long id);

UserDto updateProfile(Long id, UserDtoEntrada profileDetails);

UserDto createInitialProfile(Long id, String username);

void banUserInternal(Long userId);

}
