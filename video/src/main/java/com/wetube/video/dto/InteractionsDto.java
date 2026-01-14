package com.wetube.video.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteractionsDto {
private List<CommentsDto> comments;
private long likes;
}
