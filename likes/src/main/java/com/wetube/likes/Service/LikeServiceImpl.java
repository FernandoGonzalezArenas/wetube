package com.wetube.likes.Service;

import com.wetube.likes.dto.IdsDto;
import com.wetube.likes.dto.VideoLikeStatusDto;
import com.wetube.likes.entity.LikeEntity;
import com.wetube.likes.repository.LikeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeServiceImpl implements LikeService{

private final LikeRepository likeRepository;

@Autowired
    public LikeServiceImpl(LikeRepository likeRepository){
    this.likeRepository=likeRepository;
}

//agregar o eliminar like de el video
    @Override
    @Transactional
public boolean toggleLike(Long videoId){
Long userId= (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (videoId<=0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "videoId invalido o nulo");
        }

    Optional<LikeEntity> existingLike=likeRepository.findByUserIdAndVideoId(userId, videoId);
if (existingLike.isPresent()){
    likeRepository.delete(existingLike.get());
    return false;
}else{
    LikeEntity nuevoLike=LikeEntity.builder()
            .videoId(videoId)
            .userId(userId)
            .build();
    likeRepository.save(nuevoLike);
    return true;
}
}

@Override
public Boolean hasUserLiked(Long videoId){
    if (videoId<=0){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "videoId invalido o nulo");
    }
var auth=SecurityContextHolder.getContext().getAuthentication();
    if (auth==null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")){
        return false;
    }
Long userId=(Long) auth.getPrincipal();
    return likeRepository.existsByUserIdAndVideoId(userId, videoId);
}

//cuenta los likes de el video
@Override
public  long countLikes(Long videoId){
    if (videoId<=0){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "videoId invalido o nulo");
    }
    return likeRepository.countByVideoId(videoId);
}

@Override
    public VideoLikeStatusDto getVideoLikeStatus(Long videoId){
    Long total=countLikes(videoId);
    boolean liked=hasUserLiked(videoId);
    return new VideoLikeStatusDto(total, liked);
}

@Override
    public IdsDto getLikesVideosByUserId(Long userId){
//se obtienen los videos a los que el usuario dio like
    List<LikeEntity> result=likeRepository.findByUserId(userId);

//se crea una lista con los videoId de la lista de entidades obtenida previamente
    List<Long> videoIds=result.stream()
            .map(LikeEntity::getVideoId)
            .collect(Collectors.toList());

    //se retorna el DTO de los id obtenidos
    return new IdsDto(videoIds);
}

}
