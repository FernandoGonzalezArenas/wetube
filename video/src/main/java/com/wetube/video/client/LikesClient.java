package com.wetube.video.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "likes-service")
public interface LikesClient {

    @GetMapping("/like/{videoId}/count")
    long countLikes(@PathVariable("videoId") Long videoId);
}
