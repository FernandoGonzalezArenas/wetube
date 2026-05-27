package com.wetube.video.client;

import com.wetube.video.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClient {

@GetMapping("/users/{id}")
    UserDto getProfile(@PathVariable("id") Long id);

}
