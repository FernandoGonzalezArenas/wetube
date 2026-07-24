package com.teakter.auth.config;

import com.teakter.auth.security.CustomUserDetailsService;
import com.teakter.auth.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception{
    return http
    .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/webjars/**").permitAll()
                    .requestMatchers("/actuator/**", "/auth/register", "/auth/login", "/auth/refresh", "/auth/logout").permitAll()
                    .anyRequest().authenticated())
            .authenticationManager(authenticationManager)
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
}

@Bean
    public BCryptPasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
}

@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, DaoAuthenticationProvider authenticationProvider) throws Exception{
    return http.getSharedObject(AuthenticationManagerBuilder.class)
            .authenticationProvider(authenticationProvider)
            .build();
}

@Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder){
    DaoAuthenticationProvider provider=new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    return provider;
}
}
