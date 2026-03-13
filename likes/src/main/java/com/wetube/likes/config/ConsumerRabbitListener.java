package com.wetube.likes.config;

import com.wetube.likes.Service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerRabbitListener {

private final LikeService likeService;

@RabbitListener(queues = "delete.video.likes.queue")
    public void consumerDeleteLikes(Long videoId){
likeService.deleteLikesVideo(videoId);
}

}
