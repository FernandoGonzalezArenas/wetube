package com.teakter.likes.config;

import com.teakter.likes.Service.LikeService;
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

@RabbitListener(queues = "user.ban.likes.queue")
    private void consumerUserBanned(Long userId){
    likeService.deleteLikesByUserId(userId);
}

}
