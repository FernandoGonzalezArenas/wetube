package com.teakter.comments.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCommentDto {

    @Schema(description = "nuevo contenido de el comentario", example = "este es el nuevo contenido de el comentario")
    @NotBlank(message = "el comentario debe tener contenido")
    @Size(min = 1, max = 1200)
    private String content;

}
