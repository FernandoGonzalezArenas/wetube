package com.teakter.auth.config;

import com.teakter.auth.repository.RefreshTokenRepository;
import com.teakter.auth.repository.UserRepository;
import com.teakter.auth.service.AuthService;
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
