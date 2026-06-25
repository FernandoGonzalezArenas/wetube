package com.wetube.user.service;

import com.wetube.user.dto.UploadUrlResponse;
import com.wetube.user.dto.UserDto;
import com.wetube.user.dto.UserDtoEntrada;
import com.wetube.user.entity.UserEntity;

import java.util.List;

public interface UserService {

UserDto getProfile(Long id);

UploadUrlResponse getUploadUrl(String filename);

UserDto updateProfile(UserDtoEntrada profileDetails);

UserDto createInitialProfile(Long id, String username);

List<UserDto> getProfilesBatch(List<Long> userIds);

void banUserInternal(Long userId);

}
