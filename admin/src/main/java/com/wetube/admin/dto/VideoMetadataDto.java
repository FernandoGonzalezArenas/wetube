package com.wetube.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VideoMetadataDto {
private String title;
private String description;
private String videoUrl;
private String thumbnailUrl;

}
