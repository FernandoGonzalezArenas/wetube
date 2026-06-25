package com.wetube.comments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEntity {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

@Column(nullable = false)
    private Long videoId;

@Column(nullable = false)
private Long userId;

@Column(nullable = false)
    private String usernameAuthor;

@Column(nullable = false, length = 1200)
    private String content;

@Column(nullable = false)
    private LocalDateTime createdAt;

@Column(nullable = false)
    private LocalDateTime updatedAt;

@PrePersist
    protected void  onCreate(){
createdAt=updatedAt=LocalDateTime.now();
}

@PreUpdate
    protected void  onUpdate(){
    updatedAt=LocalDateTime.now();
}

}
