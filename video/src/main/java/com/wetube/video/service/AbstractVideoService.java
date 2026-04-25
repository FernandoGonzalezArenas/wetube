package com.wetube.video.service;

import com.wetube.video.dto.*;
import com.wetube.video.entity.VideoEntity;
import com.wetube.video.repository.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract  class AbstractVideoService implements VideoService{

    protected final VideoRepository videoRepository;
protected final  InteractionsService interactionsService;
    protected static final Logger logger= LoggerFactory.getLogger(MinioVideoServiceImpl.class);

    public abstract UploadUrlResponse generateUploadUrl(String filename);

    public abstract UploadUrlResponse generateUploadUrlThumb(String filename);

    //metodo para guardar los metadatos de el video
    @Override
    public VideoDto saveVideoMetadata(VideoDtoEntrada entrada){
UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
Long userId=principal.userId();

        VideoEntity video = new VideoEntity();
            video.setUserId(userId);
            video.setTitle(entrada.getTitle());
            video.setDescription(entrada.getDescription());
            video.setDuration(entrada.getDuration());
            video.setVideoUrl(entrada.getFilename());
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
         Long totalComments = interactionsService.countCommentsInVideo(videoId);
            LikeStatus status=interactionsService.likeStatusInVideo(videoId);

        return new InteractionsDto(comments, totalComments, status);
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
String thumbUrl=buildFullThumbnailUrl(video.getThumbnailUrl());

        //retornamos el DTO
        return VideoPlaybackDto.builder()
                .id(video.getId())
                .userId(video.getUserId())
                .title(video.getTitle())
                .description(video.getDescription())
                .duration(video.getDuration())
                .videoUrl(urlFinal)
                .thumbnailUrl(thumbUrl)
                .createdAt(video.getCreatedAt())
                .build();
    }

    //metodo abstracto para construir la URL personalizada con cada servicio de almacenamiento
    protected abstract String getPlaybackUrl(String storedUrl);


    @Override
    public List<VideoDto> getSubscriptionsFeed(){
UserPrincipal principal=(UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
Long userId=principal.userId();

List<Long> followedIds=interactionsService.getSubscriptionsByUser(userId);
if (followedIds.isEmpty()) return Collections.emptyList();

List<VideoEntity> result=videoRepository.findByUserIdInOrderByCreatedAtDesc(followedIds);

return result.stream()
        .map(this::mapToDto)
        .collect(Collectors.toList());
    }

@Override
@Transactional
public void deleteVideoInternal(Long videoId){
    Authentication auth=SecurityContextHolder.getContext().getAuthentication();

    boolean isAdmin=auth.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
if (!isAdmin){
    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "acceso denegado, no tienes derechos de administrador para borrar el video");
}

if (!videoRepository.existsById(videoId)){
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ocurrio un error, el video que se quiere eliminar no existe");
}
videoRepository.deleteById(videoId);
    }

    @Override
    public VideoDto videoInternalDetails(Long videoId){
Authentication auth=SecurityContextHolder.getContext().getAuthentication();

boolean isAdmin=auth.getAuthorities().stream()
        .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
if (!isAdmin){
    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "acceso denegado, se requieren permisos de administrador para borrar el video");
}

        VideoEntity video=videoRepository.findById(videoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "video no encontrado"));

        VideoPlaybackDto playbackDto=getVideoForPlayback(videoId);
        VideoDto dto=mapToDto(video);

        dto.setVideoUrl(playbackDto.getVideoUrl());

return dto;
    }

    protected VideoDto mapToDto(VideoEntity video){
        String thumbUrl=buildFullThumbnailUrl(video.getThumbnailUrl());
        return new VideoDto(video.getId(), video.getUserId(), video.getTitle(), video.getDescription(), video.getDuration(), video.getVideoUrl(), thumbUrl, video.getCreatedAt());
    }

    protected abstract String buildFullVideoUrl(String filename);

    protected abstract String buildFullThumbnailUrl(String filename);

}
