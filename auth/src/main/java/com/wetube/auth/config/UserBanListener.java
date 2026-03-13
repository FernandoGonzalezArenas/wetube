package com.wetube.auth.config;

import com.wetube.auth.repository.RefreshTokenRepository;
import com.wetube.auth.repository.UserRepository;
import com.wetube.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBanListener {

private final AuthService authService;

@RabbitListener(queues = "auth.user.ban.queue")
    public void handleUserBan(Long userId){
authService.banUser(userId);
}

}
