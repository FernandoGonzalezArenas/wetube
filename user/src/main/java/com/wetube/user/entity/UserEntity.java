package com.wetube.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
@Setter
        @Getter
        @NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
@Id
    private Long id;
private String username;
private String bio;

@Builder.Default
private Boolean privacyLikes=false;

    @Builder.Default
    private  Boolean privacySubs=false;
private String profilePictureUrl;
private LocalDateTime createdAt;

}
