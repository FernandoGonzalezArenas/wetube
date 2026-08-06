package com.teakter.notification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

public static final String USER_EXCHANGE = "user.exchange";
public static final String EMAIL_VERIFY_QUEUE = "user.email.verify.queue";
public static final String EMAIL_VERIFY_RK = "user.email.verify.rk";

@Bean
    public TopicExchange userExchange(){
    return new TopicExchange(USER_EXCHANGE);
}

@Bean
    public Queue emailVerifyQueue(){
    return new Queue(EMAIL_VERIFY_QUEUE, true);
}
@Bean
    public Binding emailVerifyBinding(Queue emailVerifyQueue, TopicExchange userExchange){
    return BindingBuilder.bind(emailVerifyQueue)
            .to(userExchange)
            .with(EMAIL_VERIFY_RK);
}

@Bean
    public Jackson2JsonMessageConverter messageConverter(){
return new Jackson2JsonMessageConverter();
}

}
