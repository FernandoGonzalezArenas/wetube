package com.wetube.user.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

    private String username;
    private String bio;
    private String profilePictureUrl;
    private LocalDateTime createdAt;

}
