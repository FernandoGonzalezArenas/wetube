package com.wetube.video.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoPlaybackDto {

private Long id;
private String title;
private String description;
private String videoUrl;
private String thumbnailUrl;

}
