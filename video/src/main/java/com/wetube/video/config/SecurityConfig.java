package com.wetube.video.config;

import com.wetube.video.security.GatewayHeaderFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
http
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sesion -> sesion.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
                .requestMatchers("/videos/internal/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/videos/search/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/videos/feed/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/videos/interactions/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/videos/*/play").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated())
        .addFilterBefore(new GatewayHeaderFilter(), UsernamePasswordAuthenticationFilter.class);
return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new InMemoryUserDetailsManager();
    }

}
