package com.wetube.video.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeStatus {

    @Schema(description = "numero de likes de el video", example = "162")
    private Long totalLikes;

    @Schema(description = "estado boleano de el like", example = "true")
    private boolean isLikedByUser;


}
