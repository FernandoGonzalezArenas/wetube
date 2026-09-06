package com.teakter.video.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

public static final String ADMIN_EXCHANGE = "admin.exchange";
public static final String VIDEO_EXCHANGE ="video.exchange";
public static final String USER_BAN_VIDEO_QUEUE = "user.ban.video.queue";
public static final String USER_BAN_RK = "user.baned";
public static final String VIDEO_DELETE_RK = "video.deleted";
public static final String VIDEO_PROCESS_HLS_RK="video.process.hls";
public static final String VIDEO_PROCESS_HLS_QUEUE = "video.process.hls.queue";

@Bean
public TopicExchange adminExchange(){
    return new TopicExchange(ADMIN_EXCHANGE);
}

//punto central emisor que cordina las ordenes a quien escuche
@Bean
public TopicExchange videoExchange(){
    return new TopicExchange(VIDEO_EXCHANGE);
}

@Bean
    public Queue userBanVideoQueue(){
    return new Queue(USER_BAN_VIDEO_QUEUE, true);
}

@Bean
    public Binding bindingUserBanVideo(Queue userBanVideoQueue, TopicExchange adminExchange){
    return BindingBuilder.bind(userBanVideoQueue).to(adminExchange).with(USER_BAN_RK);
}

@Bean
public Queue videoProcessHlsQueue(){
    return  new Queue(VIDEO_PROCESS_HLS_QUEUE, true);
}

@Bean
public Binding bindingVideoProcessHls(Queue videoProcessHlsQueue, TopicExchange videoExchange){
    return BindingBuilder.bind(videoProcessHlsQueue)
            .to(videoExchange)
            .with(VIDEO_PROCESS_HLS_RK);
}

@Bean
    public Jackson2JsonMessageConverter messageConverter(){
    return new Jackson2JsonMessageConverter();
}

}
