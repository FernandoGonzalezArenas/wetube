package com.wetube.admin.client;

import com.wetube.admin.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClient {

@DeleteMapping("/users/internal/{id}")
    void banUserInternal(@PathVariable("id") Long id);

@GetMapping("/users/{userId}")
    UserProfileDto getProfileUser(@PathVariable Long userId);

}
