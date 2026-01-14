package com.wetube.comments.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentDto {

    @NotBlank(message = "el comentario debe tener contenido")
    @Size(min = 1, max = 1200)
    private String content;

}
