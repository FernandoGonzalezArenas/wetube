package com.wetube.user.controller;

import com.wetube.user.dto.UserDto;
import com.wetube.user.dto.UserDtoEntrada;
import com.wetube.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service){
        this.service=service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getPublicProfile(@PathVariable Long id){
        return ResponseEntity.ok(service.getProfile(id));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(@RequestHeader("X-User-Id") Long userId, @RequestBody UserDtoEntrada profile){
        return ResponseEntity.ok(service.updateProfile(userId, profile));
    }

}
