package com.wetube.video.service;

import com.wetube.video.client.CommentsClient;
import com.wetube.video.client.LikesClient;
import com.wetube.video.dto.CommentsDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InteractionsService {

private  final CommentsClient commentsClient;
private final LikesClient likesClient;

public InteractionsService(CommentsClient commentsClient, LikesClient likesClient){
    this.commentsClient=commentsClient;
    this.likesClient=likesClient;
}

    //metodos circuitbreaker para controlar las fayas de microservicios a los que se llama
    @CircuitBreaker(name = "comment-service", fallbackMethod = "fallbackForComments")
    public List<CommentsDto> getCommentsByVideo(Long videoId){
        return commentsClient.getCommentsByVideo(videoId);
    }

    @CircuitBreaker(name = "like-service", fallbackMethod = "fallbackForLikes")
    public long countLikes(Long videoId){
        return likesClient.countLikes(videoId);
    }

    public List<CommentsDto> fallbackForComments(Long videoId, Throwable throwable){
        return List.of(new CommentsDto("sistema", "comentarios temporalmente no disponibles (Resilience4J fallback)", null, null));
    }

    public  long fallbackForLikes(Long videoId, Throwable throwable){
        return 0;
    }

}
