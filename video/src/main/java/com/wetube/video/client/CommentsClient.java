package com.wetube.video.client;

import com.wetube.video.dto.CommentsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "comment-service")
public interface CommentsClient {

@GetMapping("/comentarios/{videoId}")
    List<CommentsDto> getCommentsByVideo(@PathVariable("videoId") Long videoId);
}
