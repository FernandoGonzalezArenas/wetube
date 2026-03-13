package com.wetube.auth.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //nombre de la cola para registros nuevos (comunicacion auth-emisor, user-reseptor)
public static final String USER_EXCHANGE = "user.exchange";

//routing key para el registro de usuarios
    public static final String USER_CREATE_RK="user.create";

//cola donde el reseptor escuchara a el microservicio emisor (admin-emisor, auth-reseptor)
public static final String AUTH_BAN_QUEUE="auth.user.ban.queue";

//mismo nombre de exchange en los 2 microsservicios para que se encuentren
public static final String ADMIN_EXCHANGE="admin.exchange";

//oficina central para la creacion de usuarios y perfiles
@Bean
    public TopicExchange userExchange(){
return new TopicExchange(USER_EXCHANGE);
}

//buzon personal de auth para resibir baneos
@Bean
public Queue banQueue(){
    return new Queue(AUTH_BAN_QUEUE, true);
}

//creacion de el exchange identico como en el micro admin
@Bean
public TopicExchange adminExchange(){
    return new TopicExchange(ADMIN_EXCHANGE);
}

//aqui se conecta todo, el buzon especifico (banQueue), con la oficina central (adminExchange) y que solo entregue los mensajes que digan (user.baned)
@Bean
public Binding bindingBan(Queue banQueue, TopicExchange adminExchange){
    return BindingBuilder.bind(banQueue)
            .to(adminExchange) //exchange identico en emisor y reseptor
            .with("user.baned"); //routing key
}

//traductor de objetos a JSON
@Bean
    public Jackson2JsonMessageConverter messageConverter(){
    return new Jackson2JsonMessageConverter();
}

}
