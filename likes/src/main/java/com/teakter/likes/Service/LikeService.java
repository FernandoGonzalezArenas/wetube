package com.teakter.likes.Service;

import com.teakter.likes.dto.IdsDto;
import com.teakter.likes.dto.VideoLikeStatusDto;

public interface LikeService {
    boolean toggleLike(Long videoId);
    Boolean hasUserLiked(Long videoId);
    long countLikes(Long videoId);

     VideoLikeStatusDto getVideoLikeStatus(Long videoId);

     IdsDto getLikesVideosByUserId(Long userId);

void deleteLikesVideo(Long videoId);

void deleteLikesByUserId(Long userId);
}
