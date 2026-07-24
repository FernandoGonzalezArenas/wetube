package com.teakter.user.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //nombre de la oficina de correos identica en los dos microservicios (aut-emisor, user-reseptor)
    public static final String USER_EXCHANGE="user.exchange";

    //nombre de la cola donde se resibiran los mensajes de creacion de perfil
    public static final String USER_CREATE_QUEUE="user.profile.create.queue";

    @Bean
    public TopicExchange userExchange(){
        return new TopicExchange(USER_EXCHANGE);
    }

@Bean
public Queue createQueue(){
        return new Queue(USER_CREATE_QUEUE, true);
}

@Bean
public Binding bindingCreate(Queue createQueue, TopicExchange userExchange){
        return BindingBuilder.bind(createQueue)
                .to(userExchange)
                .with("user.create");
}

@Bean
    public Jackson2JsonMessageConverter messageConverter(){
    return new Jackson2JsonMessageConverter();
}

}
