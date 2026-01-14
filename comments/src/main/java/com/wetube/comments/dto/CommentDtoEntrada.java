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
public class CommentDtoEntrada {

    private Long videoId;

    @NotBlank(message = "debes ingresar el contenido de el mensaje")
    @Size(min = 1, max = 1200)
    private String content;

}
