package com.wetube.auth.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
private String email;

private String address;
private String phone;

@CreationTimestamp
@Column(nullable=false, updatable=false)
private LocalDateTime createdAt;

@UpdateTimestamp
@Column(nullable=false)
private LocalDateTime updateAt;
}
