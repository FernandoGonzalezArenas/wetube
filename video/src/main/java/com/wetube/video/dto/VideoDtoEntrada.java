package com.wetube.video.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDtoEntrada {

    @NotBlank(message = "el video debe tener un titulo")
    @Size(min = 1, max = 100)
    private String title;

    @NotBlank(message = "escribe una descripcion de el video")
    @Size(min = 1, max = 1000)
    private String description;
    private String filename;
    private String thumbnailUrl;
}
