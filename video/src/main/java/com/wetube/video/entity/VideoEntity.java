package com.wetube.video.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false)
private Long userId;

@Column(nullable = false)
    private String title;

@Column(nullable = false)
    private String description;

@Column(nullable = false)
    private String videoUrl;

private String thumbnailUrl;

@Column(nullable = false)
    private LocalDateTime createdAt;

@PrePersist
    protected void onCreate(){
    createdAt=LocalDateTime.now();
}
}
