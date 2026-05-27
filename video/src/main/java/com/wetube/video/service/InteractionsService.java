package com.wetube.video.service;

import com.wetube.video.client.CommentsClient;
import com.wetube.video.client.LikesClient;
import com.wetube.video.client.SubscriptionsClient;
import com.wetube.video.dto.CommentsDto;
import com.wetube.video.dto.LikeStatus;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InteractionsService {

private  final CommentsClient commentsClient;
private final LikesClient likesClient;
private final SubscriptionsClient subscriptionsClient;

    //metodos circuitbreaker para controlar las fayas de microservicios a los que se llama
    @CircuitBreaker(name = "comments", fallbackMethod = "fallbackForComments")
    public List<CommentsDto> getCommentsByVideo(Long videoId, Long lastId, Integer limit){
        return commentsClient.getCommentsByVideo(videoId, lastId, limit);
    }

    public List<CommentsDto> fallbackForComments(Long videoId, Long lastId, Integer limit, Throwable throwable){
        return List.of(CommentsDto.builder().usernameAuthor("sistema").content("comentarios temporalmente no disponibles... ").build());
    }

    @CircuitBreaker(name = "comments", fallbackMethod = "fallbackCountComments")
    public Long countCommentsInVideo(Long videoId){
        return commentsClient.countCommentsInVideo(videoId);
    }

    public Long fallbackCountComments(Long videoId, Throwable throwable){
        return 0L;
    }

    @CircuitBreaker(name = "likes", fallbackMethod = "fallbackForLikes")
    public LikeStatus likeStatusInVideo(Long videoId){
        return likesClient.likeStatusInVideo(videoId);
    }

    public LikeStatus fallbackForLikes(Long videoId, Throwable throwable){
        return new LikeStatus(0L, false);
    }


    }
