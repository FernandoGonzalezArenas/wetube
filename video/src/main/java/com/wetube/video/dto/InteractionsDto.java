package com.wetube.video.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionsDto {

    @Schema(description = "lista de comentarios de un video")
private List<CommentsDto> comments;

    @Schema(description = "numero de likes de un video", example = "128")
private long likes;
}
