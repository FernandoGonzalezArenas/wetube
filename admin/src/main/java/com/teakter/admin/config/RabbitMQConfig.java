package com.teakter.admin.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

public static final String ADMIN_EXCHANGE="admin.exchange";
public static final String VIDEO_DELETE_RK="video.deleted"; //routing key para video
public static final String USER_BAN_RK="user.baned"; //routing key para user

@Bean
    public TopicExchange adminExchange(){
return new TopicExchange(ADMIN_EXCHANGE);
}

@Bean
    public Jackson2JsonMessageConverter messageConverter(){
    return new Jackson2JsonMessageConverter();
}

}
