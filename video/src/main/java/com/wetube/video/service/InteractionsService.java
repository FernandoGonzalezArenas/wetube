package com.wetube.video.service;

import com.wetube.video.client.CommentsClient;
import com.wetube.video.client.LikesClient;
import com.wetube.video.client.SubscriptionsClient;
import com.wetube.video.dto.CommentsDto;
import com.wetube.video.dto.IdsDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class InteractionsService {

private  final CommentsClient commentsClient;
private final LikesClient likesClient;
private final SubscriptionsClient subscriptionsClient;

public InteractionsService(CommentsClient commentsClient, LikesClient likesClient, SubscriptionsClient subscriptionsClient){
    this.commentsClient=commentsClient;
    this.likesClient=likesClient;
    this.subscriptionsClient=subscriptionsClient;
}

    //metodos circuitbreaker para controlar las fayas de microservicios a los que se llama
    @CircuitBreaker(name = "comments", fallbackMethod = "fallbackForComments")
    public List<CommentsDto> getCommentsByVideo(Long videoId, Long lastId, Integer limit){
        return commentsClient.getCommentsByVideo(videoId, lastId, limit);
    }

    public List<CommentsDto> fallbackForComments(Long videoId, Long lastId, Integer limit, Throwable throwable){
        return List.of(CommentsDto.builder().usernameAuthor("sistema").content("comentarios temporalmente no disponibles... ").build());
    }

    @CircuitBreaker(name = "likes", fallbackMethod = "fallbackForLikes")
    public long countLikes(Long videoId){
        return likesClient.countLikes(videoId);
    }

    public  long fallbackForLikes(Long videoId, Throwable throwable){
        return 0;
    }

    @CircuitBreaker(name = "user", fallbackMethod = "fallbackForUser")
    public List<Long> getSubscriptionsByUser(Long userId){
return subscriptionsClient.getSubscriptionsByUser(userId).getIds();
    }

    public List<Long> fallbackForUser(Long userId, Throwable throwable){
    return Collections.emptyList();
    }

}
