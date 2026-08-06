package com.teakter.auth.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(unique = true, nullable = false)
    private String username;

@Column(nullable = false)
    private String password;

@Column(nullable = false)
@Builder.Default
private String role="ROLE_USER";

@Column(nullable = false)
private String email;

private String address;
private String phone;

@Column(nullable = false)
@Builder.Default
private Boolean isVerified = false;

@CreationTimestamp
@Column(nullable=false, updatable=false)
private LocalDateTime createdAt;

@UpdateTimestamp
@Column(nullable=false)
private LocalDateTime updateAt;

@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private VerificationTokenEntity verificationToken;

@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RefreshTokenEntity> refreshTokens;

}
