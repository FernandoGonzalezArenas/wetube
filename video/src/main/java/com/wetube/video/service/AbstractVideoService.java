package com.wetube.video.service;

import com.wetube.video.dto.*;
import com.wetube.video.entity.VideoEntity;
import com.wetube.video.repository.VideoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
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

    @Override
    public List<VideoDto> getVideosByIds(IdsDto ids){
        List<VideoEntity> results=videoRepository.findByIdIn(ids.getIds());

        //convertimos la lista de entidades a lista de DTO correctamente y la retornamos
        return results.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public VideoPlaybackDto getVideoForPlayback(Long videoId){
        //buscamos el video en la base de datos
        VideoEntity video=videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "video no encontrado"));

        //generamos la URL de acceso
        String urlFinal=getPlaybackUrl(video.getVideoUrl());

        //retornamos el DTO
        return VideoPlaybackDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .description(video.getDescription())
                .videoUrl(urlFinal)
                .thumbnailUrl(video.getThumbnailUrl())
                .build();
    }

    //metodo abstracto para construir la URL personalizada con cada servicio de almacenamiento
    protected abstract String getPlaybackUrl(String storedUrl);

    @Override
    public List<VideoDto> getSubscriptionsFeed(){
Long userId=(Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
List<Long> followedIds=interactionsService.getSubscriptionsByUser(userId);
if (followedIds.isEmpty()) return Collections.emptyList();

return videoRepository.findByUserIdInOrderByCreatedAtDesc(followedIds);
    }

    protected VideoDto mapToDto(VideoEntity video){
        return new VideoDto(video.getTitle(), video.getDescription(), video.getVideoUrl(), video.getThumbnailUrl());
    }

    protected abstract String buildFullVideoUrl(String filename);

}
