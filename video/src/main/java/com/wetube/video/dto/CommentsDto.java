package com.wetube.video.dto;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentsDto {
private String usernameAuthor;
private String content;
private LocalDateTime createdAt;
private LocalDateTime updatedAt;
}
