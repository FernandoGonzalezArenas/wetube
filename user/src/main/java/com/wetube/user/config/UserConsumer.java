package com.wetube.user.config;

import com.wetube.user.dto.UserRabbitDto;
import com.wetube.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserConsumer {

private final UserService userService;

@RabbitListener(queues = "user.profile.create.queue")
    public void consumeRegistrationMessage(UserRabbitDto message){
    log.info("mensaje resibido desde rabbitMQ: {}, {}", message.getUserId(), message.getUsername());
    Long id=message.getUserId();
    String username=message.getUsername();

    //llamamos a el metodo para crear el perfil
    userService.createInitialProfile(id, username);
}

}
