package com.wetube.video.service;

import com.wetube.video.dto.*;
import com.wetube.video.entity.VideoEntity;
import com.wetube.video.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

public abstract  class AbstractVideoService implements VideoService{

    protected final VideoRepository videoRepository;
protected final  InteractionsService interactionsService;
    protected static final Logger logger= LoggerFactory.getLogger(MinioVideoServiceImpl.class);

    @Autowired
    public AbstractVideoService(VideoRepository videoRepository, InteractionsService interactionsService){
        this.videoRepository=videoRepository;
this.interactionsService=interactionsService;
    }

    public abstract UploadUrlResponse generateUploadUrl(String filename);

    //metodo para guardar los metadatos de el video
    @Override
    public VideoDto saveVideoMetadata(VideoDtoEntrada entrada){
        String videoUrl = buildFullVideoUrl(entrada.getFilename());
Long userId=(Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        VideoEntity video = new VideoEntity();
            video.setUserId(userId);
            video.setTitle(entrada.getTitle());
            video.setDescription(entrada.getDescription());
            video.setVideoUrl(videoUrl);
            video.setThumbnailUrl(entrada.getThumbnailUrl());
            videoRepository.save(video);

            return mapToDto(video);
        }

    @Override
    public Page<VideoDto> searchVideosByTitle(String keyword, int page, int size){
        PageRequest pageable=PageRequest.of(page, size);
        Page<VideoEntity> result= videoRepository.searchByTitle(keyword, pageable);
return result.map(this::mapToDto);
    }

    @Override
    public List<VideoDto> getFeed(Long lastId, int limit){
        PageRequest pageable=PageRequest.of(0, limit);
        List<VideoEntity> videos= videoRepository.findNextVideos(lastId, pageable);
        return videos.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public InteractionsDto getInteractions(Long videoId, Long lastId, Integer limit){
         List<CommentsDto> comments=interactionsService.getCommentsByVideo(videoId, lastId, limit);
            long likes=interactionsService.countLikes(videoId);

        return new InteractionsDto(comments, likes);
    }

    protected VideoDto mapToDto(VideoEntity video){
        return new VideoDto(video.getTitle(), video.getDescription(), video.getVideoUrl(), video.getThumbnailUrl());
    }

    protected abstract String buildFullVideoUrl(String filename);

}
