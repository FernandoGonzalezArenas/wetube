package com.wetube.likes.Service;

import com.wetube.likes.dto.IdsDto;
import com.wetube.likes.dto.VideoLikeStatusDto;

public interface LikeService {
    boolean toggleLike(Long videoId);
    Boolean hasUserLiked(Long videoId);
    long countLikes(Long videoId);

     VideoLikeStatusDto getVideoLikeStatus(Long videoId);

     IdsDto getLikesVideosByUserId();

void deleteLikesVideo(Long videoId);

}
