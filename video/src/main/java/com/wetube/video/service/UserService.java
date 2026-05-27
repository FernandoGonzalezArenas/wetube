package com.wetube.video.service;

import com.wetube.video.client.UserClient;
import com.wetube.video.dto.UserDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

private final UserClient userClient;

@CircuitBreaker(name = "user", fallbackMethod = "fallbackForProfile")
    public UserDto getProfile(Long userId){
    return userClient.getProfile(userId);
}

public  UserDto fallbackForProfile(Long userId, Throwable throwable){
    return UserDto.builder().id(0L).username("sistema").bio("usuario no disponible").build();
}

}
