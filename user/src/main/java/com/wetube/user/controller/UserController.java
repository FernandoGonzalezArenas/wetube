package com.wetube.user.controller;

import com.wetube.user.dto.UploadUrlResponse;
import com.wetube.user.dto.UserDto;
import com.wetube.user.dto.UserDtoEntrada;
import com.wetube.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

    @GetMapping("/upload-ppu")
    public ResponseEntity<UploadUrlResponse> getUploadUrl(@RequestParam String filename){
        UploadUrlResponse response=service.getUploadUrl(filename);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserDto> updateMyProfile(@RequestBody UserDtoEntrada profile){
        return ResponseEntity.ok(service.updateProfile(profile));
    }

    @DeleteMapping("/internal/{id}")
    public ResponseEntity<String> banUserInternal(@PathVariable Long id){
        service.banUserInternal(id);
        return ResponseEntity.ok("la cuenta fue borrada correctamente por un administrador");
    }

}
