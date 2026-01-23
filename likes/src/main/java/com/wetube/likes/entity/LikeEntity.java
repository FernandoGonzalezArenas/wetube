package com.wetube.likes.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes", uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "videoId"}))
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false)
    private Long videoId;

@Column(nullable = false)
    private Long userId;

}
