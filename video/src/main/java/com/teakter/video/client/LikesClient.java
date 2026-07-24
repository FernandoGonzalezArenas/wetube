package com.teakter.video.client;

import com.teakter.video.dto.IdsDto;
import com.teakter.video.dto.LikeStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "likes")
public interface LikesClient {

    @GetMapping("/like/{videoId}/status")
    LikeStatus likeStatusInVideo(@PathVariable("videoId") Long videoId);

    @GetMapping("/like/user-likes/{userId}")
    IdsDto likedVideos(@PathVariable("userId") Long userId);
}
