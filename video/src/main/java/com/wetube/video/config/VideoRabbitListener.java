package com.wetube.video.config;

import com.wetube.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VideoRabbitListener {

private final VideoService videoService;

@RabbitListener(queues = "user.ban.video.queue")
    public void handleUserBanned(Long userId){
    videoService.processUserBannedInternal(userId);
}

}
