package com.wetube.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentsDto {

    private Long videoId;
    private String usernameAuthor;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
