package com.teakter.auth.config;

import com.teakter.auth.entity.UserEntity;
import com.teakter.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminInitializerConfig {

private final UserRepository userRepository;
private final PasswordEncoder passwordEncoder;

@Bean
    public CommandLineRunner initializeAdmin(){
    return args -> {
        String adminUsername = "fernando";
        if (userRepository.findByUsername(adminUsername).isEmpty()){
            UserEntity admin = UserEntity.builder()
                    .username(adminUsername)
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@teakter.com")
                    .role("ROLE_ADMIN")
                    .isVerified(true)
                    .address("Aguascalientes, MX")
                    .phone("4491234567")
                    .build();

            userRepository.save(admin);
            System.out.println(">>> [teakter Auth] Usuario Administrador de pruebas creado exitosamente.");
            System.out.println(">>> Username: fernando | Password: admin123");
        }
    };
}

}
