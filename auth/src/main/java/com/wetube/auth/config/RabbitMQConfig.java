package com.wetube.auth.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

public static final String REGISTER_QUEUE = "user.registration.queue";

@Bean
    public org.springframework.amqp.core.Queue queue(){
    return new Queue(REGISTER_QUEUE, true);
}

@Bean
    public Jackson2JsonMessageConverter messageConverter(){
    return new Jackson2JsonMessageConverter();
}

}
