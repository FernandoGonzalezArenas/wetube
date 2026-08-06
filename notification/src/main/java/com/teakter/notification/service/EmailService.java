package com.teakter.notification.service;

public interface EmailService {

void sendVerificationEmail(String toEmail, String username, String token);

}
