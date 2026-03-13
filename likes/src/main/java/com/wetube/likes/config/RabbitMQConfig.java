package com.wetube.likes.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

public static final String ADMIN_EXCHANGE="admin.exchange";
public static final String DELETE_LIKES_QUEUE="delete.video.likes.queue";

@Bean
    public TopicExchange adminExchange(){
    return new TopicExchange(ADMIN_EXCHANGE);
}

@Bean
    public Queue deleteLikesQueue(){
    return new Queue(DELETE_LIKES_QUEUE, true);
}

@Bean
    public Binding deleteBinding(Queue deleteLikesQueue, TopicExchange adminExchange){
    return BindingBuilder.bind(deleteLikesQueue)
            .to(adminExchange)
            .with("video.deleted");
}

@Bean
    public Jackson2JsonMessageConverter messageConverter(){
    return new Jackson2JsonMessageConverter();
}

}
