package com.wetube.likes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoLikeStatusDto {

    @Schema(description = "numero de likes de el video", example = "162")
private Long totalLikes;

    @Schema(description = "estado boleano de el like", example = "true")
private boolean isLikedByUser;
}
