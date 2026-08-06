package com.teakter.auth.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_tokens")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationTokenEntity {

@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false, unique = true)
    private String token;

@OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private UserEntity user;

@Column(nullable = false)
    private LocalDateTime expiryDate;

}
