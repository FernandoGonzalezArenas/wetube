package com.wetube.likes.controller;

import com.wetube.likes.Service.LikeService;
import com.wetube.likes.dto.IdsDto;
import com.wetube.likes.dto.VideoLikeStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {

private final LikeService likeService;

@PostMapping("/{videoId}/toggle")
    public ResponseEntity<Void> toggleLike(@PathVariable Long videoId){
    boolean creado=likeService.toggleLike(videoId);
    if (creado){
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }else {
        return ResponseEntity.noContent().build();
    }
}

@GetMapping("/{videoId}/count")
    public ResponseEntity<Long> countLikes(@PathVariable Long videoId){
    return ResponseEntity.ok(likeService.countLikes(videoId));
}

@GetMapping("/{videoId}/status")
    public ResponseEntity<VideoLikeStatusDto> getLikeStatus(@PathVariable Long videoId){
    return ResponseEntity.ok(likeService.getVideoLikeStatus(videoId));
}

@GetMapping("/me")
    public ResponseEntity<IdsDto> getLikesVideosByUserId(){
    IdsDto videoIds=likeService.getLikesVideosByUserId();
  return ResponseEntity.ok(videoIds);
}

}
