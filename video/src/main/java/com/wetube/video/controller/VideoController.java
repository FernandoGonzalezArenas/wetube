package com.wetube.video.controller;

import java.util.List;
import java.util.Map;

import com.wetube.video.dto.UploadUrlResponse;
import com.wetube.video.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wetube.video.dto.InteractionsDto;
import com.wetube.video.dto.VideoDto;
import com.wetube.video.dto.VideoDtoEntrada;
import com.wetube.video.service.VideoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/videos")
public class VideoController {

private final VideoService videoService;
private JwtUtil jwtUtil;

    public VideoController(VideoService videoService, JwtUtil jwtUtil){
    this.videoService = videoService;
    this.jwtUtil=jwtUtil;
}

//controlador para generar una URL firmada para subir un video a S3
    @GetMapping("/upload-url")
    public ResponseEntity<UploadUrlResponse> getUploadUrl(@RequestParam String filename, HttpServletRequest request){
        jwtUtil.getUseridOrThrow(request);
       UploadUrlResponse  response= videoService.generateUploadUrl(filename);
    return ResponseEntity.ok(response);
    }

//controlador para guardar los metadatos de el video despues de la subida
    @PostMapping("/save-metadata")
    public ResponseEntity<VideoDto> saveVideoMetadata(@Valid @RequestBody VideoDtoEntrada entrada, HttpServletRequest request){
    VideoDto saveVideo= videoService.saveVideoMetadata(entrada, request);
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
    public ResponseEntity<InteractionsDto> getInteractions(@PathVariable Long videoId){
try {
    return ResponseEntity.ok(videoService.getInteractions(videoId));
}catch (Exception e){
    return ResponseEntity.notFound().build();
}
    }

}
