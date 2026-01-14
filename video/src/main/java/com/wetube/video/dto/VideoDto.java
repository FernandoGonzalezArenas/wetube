package com.wetube.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoDto {
    private String title;
    private String description;
private String videoUrl;
private String thumbnailUrl;
}
