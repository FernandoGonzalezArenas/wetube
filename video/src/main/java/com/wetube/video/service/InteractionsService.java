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
    @CircuitBreaker(name = "comments", fallbackMethod = "fallbackForComments")
    public List<CommentsDto> getCommentsByVideo(Long videoId, Long lastId, Integer limit){
        return commentsClient.getCommentsByVideo(videoId, lastId, limit);
    }

    @CircuitBreaker(name = "likes", fallbackMethod = "fallbackForLikes")
    public long countLikes(Long videoId){
        return likesClient.countLikes(videoId);
    }

    public List<CommentsDto> fallbackForComments(Long videoId, Long lastId, Integer limit, Throwable throwable){
        return List.of(CommentsDto.builder().usernameAuthor("sistema").content("comentarios temporalmente no disponibles... ").build());
    }

    public  long fallbackForLikes(Long videoId, Throwable throwable){
        return 0;
    }

}
