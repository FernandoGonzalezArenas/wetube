package com.wetube.likes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoLikeStatusDto {
private Long totalLikes;
private boolean isLikedByUser;
}
