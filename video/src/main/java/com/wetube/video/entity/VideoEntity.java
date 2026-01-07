package com.wetube.video.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "videos")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
