package com.teakter.comments.config;

import com.teakter.comments.service.CommentService;
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

@RabbitListener(queues = "user.ban.comments.queue")
    public void consumerCommentsUserBanned(Long userId){
    commentService.deleteCommentsByUserId(userId);
}


}
