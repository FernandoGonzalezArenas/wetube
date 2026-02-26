package com.wetube.video.controller;

import com.wetube.video.dto.*;
import com.wetube.video.entity.VideoEntity;
import com.wetube.video.service.VideoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {

private final VideoService videoService;

    public VideoController(VideoService videoService){
    this.videoService = videoService;
}

//controlador para generar una URL firmada para subir un video a S3
    @GetMapping("/upload-url")
    public ResponseEntity<UploadUrlResponse> getUploadUrl(@RequestParam String filename){
       UploadUrlResponse  response= videoService.generateUploadUrl(filename);
    return ResponseEntity.ok(response);
    }

//controlador para guardar los metadatos de el video despues de la subida
    @PostMapping("/save-metadata")
    public ResponseEntity<VideoDto> saveVideoMetadata(@Valid @RequestBody VideoDtoEntrada entrada){
    VideoDto saveVideo= videoService.saveVideoMetadata(entrada);
    return ResponseEntity.ok(saveVideo);
    }

@GetMapping("/search")
public ResponseEntity<Page<VideoDto>> searchVideos(@RequestParam String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size){
    return ResponseEntity.ok(videoService.searchVideosByTitle(keyword, page, size));
}

@GetMapping("/feed")
public ResponseEntity<List<VideoDto>> getFeed(@RequestParam(required = false) Long lastId, @RequestParam(defaultValue = "10") int limit){
    return ResponseEntity.ok(videoService.getFeed(lastId, limit));
}

    @GetMapping("/interactions/{videoId}")
    public ResponseEntity<InteractionsDto> getInteractions(@PathVariable Long videoId, @RequestParam(value = "lastId", required = false) Long lastId, @RequestParam(value = "limit", defaultValue = "10") Integer limit){
try {
    return ResponseEntity.ok(videoService.getInteractions(videoId, lastId, limit));
}catch (Exception e){
    return ResponseEntity.notFound().build();
}
    }

    @PostMapping("/list-likes")
    public ResponseEntity<List<VideoDto>> getVideosByIds(@RequestBody IdsDto ids){
        return ResponseEntity.ok(videoService.getVideosByIds(ids));
    }

    @GetMapping("/{videoId}/play")
    public ResponseEntity<VideoPlaybackDto> playVideo(@PathVariable Long videoId){
        return ResponseEntity.ok(videoService.getVideoForPlayback(videoId));
    }

    @GetMapping("/my-feed-subs")
    public ResponseEntity<List<VideoDto>> getSubscriptionsFeed(){
        return ResponseEntity.ok(videoService.getSubscriptionsFeed());
    }

}
