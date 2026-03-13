package com.wetube.comments.config;

import com.wetube.comments.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsumerRabbitListener {

    private final CommentService commentService;

@RabbitListener(queues = "delete.user.comments.queue")
    public void consumeDeleteCommentsWithVideoId(Long videoId){
commentService.deleteCommentsWithVideoId(videoId);
}

}
