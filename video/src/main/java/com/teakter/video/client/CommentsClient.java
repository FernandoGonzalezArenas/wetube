package com.teakter.video.client;

import com.teakter.video.dto.CommentsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "comments")
public interface CommentsClient {

@GetMapping("/comentarios/{videoId}")
    List<CommentsDto> getCommentsByVideo(@PathVariable("videoId") Long videoId, @RequestParam("lastId") Long lastId, @RequestParam("limit") Integer limit);

@GetMapping("/comentarios/{videoId}/count")
    Long countCommentsInVideo(@PathVariable Long videoId);
}
