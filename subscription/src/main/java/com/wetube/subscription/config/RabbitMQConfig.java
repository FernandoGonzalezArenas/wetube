package com.wetube.subscription.config;

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
    public static final String ADMIN_EXCHANGE ="admin.exchange";

    //nombre de la cola donde se resibiran los mensajes de creacion de perfil
    public static final String USER_SUBS_QUEUE="user.subs.delete.queue";

    @Bean
    public TopicExchange adminExchange(){
        return new TopicExchange(ADMIN_EXCHANGE);
    }

    @Bean
    public Queue SubsQueue(){
        return new Queue(USER_SUBS_QUEUE, true);
    }

    @Bean
    public Binding bindingDelete(Queue subsQueue, TopicExchange adminExchange){
        return BindingBuilder.bind(subsQueue)
                .to(adminExchange)
                .with("user.baned");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}
