package com.teakter.admin.client;

import com.teakter.admin.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user")
public interface UserClient {

@DeleteMapping("/users/internal/{id}")
    void banUserInternal(@PathVariable("id") Long id);

@GetMapping("/users/details")
List<UserProfileDto> getProfileDetails(@RequestParam("ids") List<Long> userIds);

}
