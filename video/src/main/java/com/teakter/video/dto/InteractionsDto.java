package com.teakter.video.dto;

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

    @Schema(description = "numero de comentarios de un video", example = "86")
    private Long totalComments;

    @Schema(description = "numero y estatus de los likes en el video")
private LikeStatus status;
}
